package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.worker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.ServiceWorkerConfigSingle;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.singleConfig.Action;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.singleConfig.RequestRule;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.singleConfig.ResponseRule;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function.FunctionExecutor;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.utils.HttpProxyAdaptor;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.utils.JsonKeyPathObject;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.utils.JsonUtil;

import lombok.Data;

@Data
public class ServiceWorkerSingle {

private ServiceWorkerComposite serviceWorkerComposite;
	
	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigSingle config;
	
	private HttpProxyAdaptor httpProxyAdaptor;
	
	private String inputPath;
	
	private String outgoingPath;
	
	private HttpHeaders inputHttpHeaders;
	
	private HttpHeaders outgoingHttpHeaders;
	
	private Map<String, Object> inputQueryParams;
	
	private Map<String, Object> outgoingQueryParams;
	
	private String inputRequestBody;
	private DocumentContext dcInputRequestBody;
	
	private String outgoingRequestBody;
	private DocumentContext dcOutgoingRequestBody;
	
	private String inputResponseBody;
	private DocumentContext dcInputResponseBody;
	private ResponseEntity<String> inputResponseEntity;
	
	private String outgoingResponseBody;
	private DocumentContext dcOutgoingResponseBody;
	
	private ObjectMapper om = new ObjectMapper();
	
	public ServiceWorkerSingle(ServiceWorkerConfigSingle config) {
		//
		this.requestId = UUID.randomUUID().toString();
		this.config = config;
		try {
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public ServiceWorkerSingle(String requestId, ServiceWorkerConfigSingle config) {
		//
		this.requestId = requestId;
		this.config = config;
		try {
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputs(HttpHeaders inputHttpHeaders, Map<String, Object> inputQueryParams, String inputRequestBody) {
		//
		if(inputHttpHeaders != null)
			this.inputHttpHeaders = inputHttpHeaders;
		else
			this.inputHttpHeaders = new HttpHeaders();
		
		if(inputQueryParams != null)
			this.inputQueryParams = inputQueryParams;
		else
			this.inputQueryParams = new HashMap<>();
		
		if(inputRequestBody != null)
			this.inputRequestBody = inputRequestBody;
		else
			this.inputRequestBody = "{}";
		dcInputRequestBody = JsonPath.parse(this.inputRequestBody);
	}
	
	public void setInputRequestBody(String inputRequestBody) {
		//
		if(inputRequestBody != null && !inputRequestBody.isEmpty())
			this.inputRequestBody = inputRequestBody;
		else
			this.inputRequestBody = "{}";
		dcInputRequestBody = JsonPath.parse(this.inputRequestBody);
	}
	
	public void executeWork() {
		// 1)
		if (executeRequestRule()) {
			// 2) 
			executeHttpProxy();
			// 3) 
			executeResponseRule();
		}
		else {
			// 4)
			// make fixed return message for bad request
		}
	}
	
	private Boolean executeRequestRule() {
		//
		if (checkConditionAndExecuteRequestRule()) {
//			System.out.println("[ServiceWorkerSingle].executeRequestRule : condition trigger is activated.");
			return true;
		}
		else {
//			System.out.println("[ServiceWorkerSingle].executeRequestRule : any condition trigger is not activated. cut the request.");
			return false;
		}
	}
	
	private Boolean checkConditionAndExecuteRequestRule() {
		//
		List<RequestRule> requestRules = config.getConfig().getRequestRule();
		
		if (!requestRules.isEmpty()) {
			for (RequestRule iterRequestRule : requestRules) {
				if (checkCondition(iterRequestRule.getCondition())) {
					executeAction(iterRequestRule.getAction());
					return true;
				}
			}
			return false;
		}
		else {
			return true;
		}
	}
	
	private Boolean checkCondition(List<String> condition) {
		// 
		Boolean retFlag = true;
		try {
			for (String iter : condition) {
				Boolean tmpFlag = false;
				if (iter.contains("FUNCTION_")) {
					Object retObject = FunctionExecutor.executeFunctions(this, iter);
					
					if (retObject instanceof Boolean)
						tmpFlag = (Boolean)retObject;
					else
						tmpFlag = Boolean.valueOf(retObject.toString());
				}
				else {
					tmpFlag = false;
				}
//				System.out.println("[ServiceWorkerSingle].checkCondition : tmpFlag = " + tmpFlag);
				retFlag = (retFlag && tmpFlag);
			}
//			System.out.println("[ServiceWorkerSingle].checkCondition : retFlag = " + retFlag);
			return retFlag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void executeAction(Action action) {
		// 1)
		makeOutgoingPath(action);
		
		// 2)
		makeOutgoingHeaders(action);
		
		// 3)
		makeOutgoingQueryParams(action);
		
		// 4)
		makeOutgoingRequestBody(action);
	}
	
	private void makeOutgoingPath(Action action) {
		// 
		String path = action.getPath();
		String result = null;
		if (path.contains("FUNCTION_")) {
			Object retObject = FunctionExecutor.executeFunctions(this, path);
			
			if (retObject instanceof String)
				result = (String)retObject;
			else {
				if (retObject != null)
					result = retObject.toString();
				else
					result = null;
			}
		}
		else {
			result = new String(path);
		}
		
		outgoingPath = result;
	}
	
	@SuppressWarnings("unchecked")
	private void makeOutgoingHeaders(Action action) {
		//
		if (outgoingHttpHeaders == null)
			outgoingHttpHeaders = new HttpHeaders();
			
		Map<String, List<String>> headersConfig = action.getHeaders();
		if (headersConfig == null) {
			outgoingHttpHeaders = null;
		}
		else if ( (headersConfig != null) && (!headersConfig.isEmpty()) ) {
			for (Entry<String, List<String>> iterHeaders : headersConfig.entrySet()) {
				List<String> headerValues = iterHeaders.getValue();
				for (String iterHeaderValue : headerValues) {
					//
					if (iterHeaderValue.contains("FUNCTION_")) {
						String result = null;
						
						Object retObject = FunctionExecutor.executeFunctions(this, iterHeaderValue);
						
						if (retObject instanceof List) {
							List<String> list = (List<String>)retObject;
							for (String iter : list) {
								outgoingHttpHeaders.add(iterHeaders.getKey(), iter);
							}
						}
						else if (retObject instanceof String) {
							result = (String)retObject;
							outgoingHttpHeaders.add(iterHeaders.getKey(), result);
						}
						else {
							if (retObject != null)
								result = retObject.toString();
							else
								result = null;
							outgoingHttpHeaders.add(iterHeaders.getKey(), result);
						}
					}
					else {
						outgoingHttpHeaders.add(iterHeaders.getKey(), iterHeaderValue);						
					}
				}
			}
		}
		else {	// byPass : {}
			outgoingHttpHeaders = inputHttpHeaders;
		}
	}
	
	private void makeOutgoingQueryParams(Action action) {
		//
		if (outgoingQueryParams == null)
			outgoingQueryParams = new HashMap<>();
		
		Map<String, Object> queryParamsConfig = action.getQueryParams();
		if (queryParamsConfig == null) {
			outgoingQueryParams = null;
		}
		else if ( (queryParamsConfig != null) && (!queryParamsConfig.isEmpty()) ) {
			for (Entry<String, Object> iterQueryParams : queryParamsConfig.entrySet()) {
				//
				String string = iterQueryParams.getValue().toString();
				if (string.contains("FUNCTION_")) {
					outgoingQueryParams.put(iterQueryParams.getKey(), FunctionExecutor.executeFunctions(this, string));
				}
				else {
					outgoingQueryParams.put(iterQueryParams.getKey(), iterQueryParams.getValue());
				}
			}
		}
		else {	// byPass : {}
			outgoingQueryParams = inputQueryParams;
		}
	}
	
	private void makeOutgoingRequestBody(Action action) {
		//
		Object requestBodyConfig = action.getRequestBody();
		
		if (requestBodyConfig == null) {
			outgoingRequestBody = null;
		}
		else {
			List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
			JsonUtil.extractJsonKeyPathObjectFormJsonObject(requestBodyConfig, jsonPathKeyObjects);
			
//			System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : b/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
			
			if (!jsonPathKeyObjects.isEmpty()) {
				for (JsonKeyPathObject iter : jsonPathKeyObjects) {
					//
					String string = iter.getObject().toString();
					if (string.contains("FUNCTION_")) {
						iter.setObject(FunctionExecutor.executeFunctions(this, string));
					}
					else {
						iter.setObject(iter.getObject());
					}
				}
			
//				System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : a/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
	
				outgoingRequestBody = JsonUtil.mergeJsonString(jsonPathKeyObjects);
				dcOutgoingRequestBody = JsonPath.parse(this.outgoingRequestBody);
			}
			else {
				outgoingRequestBody = inputRequestBody;
				dcOutgoingRequestBody = JsonPath.parse(this.outgoingRequestBody);
			}
		}
		
//		System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : outgoingRequetBody = " + outgoingRequestBody);
	}

	private ResponseEntity<String> executeHttpProxy() {
		//
		String scheme = config.getConfig().getScheme();
		String url = config.getConfig().getHost();
		HttpMethod httpMethod = HttpMethod.valueOf(config.getConfig().getMethod());
		
		ResponseEntity<String> result = httpProxyAdaptor.executeHttpProxy(scheme, 
																		  url, 
																		  outgoingPath, 
																		  outgoingQueryParams, 
																		  httpMethod, 
																		  outgoingHttpHeaders, 
																		  outgoingRequestBody);
		
		inputResponseEntity = result;
		inputResponseBody = result.getBody();
		dcInputResponseBody = JsonPath.parse(this.inputResponseBody);
		
//		System.out.println("[ServiceWorkerSingle].executeHttpProxy : inputResponseBody = " + inputResponseBody);
		
		return result;
	}
	
	private void executeResponseRule() {
		// 5)
		if (checkConditionAndExecuteResponseRule()) {
//			System.out.println("[ServiceWorkerSingle].executeResponseRule : condition trigger is activated.");
		}
		else {
			outgoingResponseBody = inputResponseBody;
			dcOutgoingResponseBody = JsonPath.parse(this.outgoingResponseBody);
		}
	}
	
	private Boolean checkConditionAndExecuteResponseRule() {
		//
		for (ResponseRule iterResponseRule : config.getConfig().getResponseRule()) {
			if (checkCondition(iterResponseRule.getCondition())) {
				//
				makeOutgoingResponseBody(iterResponseRule.getResponseBody());
				return true;
			}
		}
		return false;
	}
	
	private void makeOutgoingResponseBody(Object responseBody) {
		//
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		JsonUtil.extractJsonKeyPathObjectFormJsonObject(responseBody, jsonPathKeyObjects);
		
//		System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : b/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			String string = iter.getObject().toString();
			if (string.contains("FUNCTION_")) {
				iter.setObject(FunctionExecutor.executeFunctions(this, string));
			}
			else {
				iter.setObject(iter.getObject());
			}
		}
		
//		System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : a/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
	
		outgoingResponseBody = JsonUtil.mergeJsonString(jsonPathKeyObjects);
		dcOutgoingResponseBody = JsonPath.parse(this.outgoingResponseBody);
		
//		System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : outgoingResponseBody = " + outgoingResponseBody);
	}
	
	// Inner Interface --------------------------------------------------------------------------------------------------
	// Http Header --------------------------
	public Object getValueOfInputHttpHeader(String key, Integer headerReference) {
		// read value of specific key path in inputHttpHeader
		if (headerReference == null)
			return this.inputHttpHeaders.get(key);
		else
			return this.inputHttpHeaders.get(key).get(headerReference);
	}

	private Object getValueOfOutgoingHttpHeader(String key, Integer headerReference) {
		// read value of specific key path in outgoingHttpHeader
		if (headerReference == null)
			return this.outgoingHttpHeaders.get(key);
		else
			return this.outgoingHttpHeaders.get(key).get(headerReference);
	}
	
	// Query Parameters ---------------------
	public Object getValueOfInputQueryParams(String key) {
		// read value of specific key path in inputHttpHeader
		return this.inputQueryParams.get(key);
	}
	
	private Object getValueOfOutgoingQueryParams(String key) {
		// read value of specific key path in outgoingHttpHeader
		return this.outgoingQueryParams.get(key);
	}
	
	// Body ---------------------------------
	public Object getValueOfInputRequestBody(String key) {
		// read value of specific key path in inputRequestBody
		return JsonUtil.readValue(dcInputRequestBody, key);
	}
	
	private Object getValueOfOutgoingRequestBody(String key) {
		// read value of specific key path in outgoingRequestBody
		return JsonUtil.readValue(dcOutgoingRequestBody, key);
	}
	
	public Object getValueOfInputResponseBody(String key) {
		// read value of specific key path in inputResponseBody
		return JsonUtil.readValue(dcInputResponseBody, key);
	}
	
	private Object getValueOfOutgoingResponseBody(String key) {
		// read value of specific key path in outgoingResponseBody
		return JsonUtil.readValue(dcOutgoingResponseBody, key);
	}
	
	// Response Status ----------------------
	public Object getValueOfResponseStatus(String key) {
		//
		if (key.toUpperCase().equals("CODE")) {
			return Integer.valueOf(inputResponseEntity.getStatusCodeValue());
		}
		else
			return null;
	}
	//-------------------------------------------------------------------------------------------------------------------
	
	// Outer Interface --------------------------------------------------------------------------------------------------
	public Object getValueOfHeaderByComposite(String key, Integer headerReference) {
		//
		return getValueOfOutgoingHttpHeader(key, headerReference);
	}
	
	public Object getValueOfQueryParamByComposite(String key) {
		//
		return getValueOfOutgoingQueryParams(key);
	}
	
	public Object getValueOfRequestBodyByComposite(String key) {
		//
		return getValueOfOutgoingRequestBody(key);
	}
	
	public Object getValueOfResponseBodyByComposite(String key) {
		//
		return getValueOfOutgoingResponseBody(key);
	}
	
	public Object getValueOfResponseStatusByComposite(String key) {
		//
		return getValueOfResponseStatus(key);
	}
	//-------------------------------------------------------------------------------------------------------------------
}