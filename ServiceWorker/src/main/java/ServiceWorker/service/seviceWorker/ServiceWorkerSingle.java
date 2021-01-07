package ServiceWorker.service.seviceWorker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigSingle;
import ServiceWorker.model.seviceWorkerConfig.config.Action;
import ServiceWorker.model.seviceWorkerConfig.config.RequestRule;
import ServiceWorker.model.seviceWorkerConfig.config.ResponseRule;
import ServiceWorker.service.seviceWorker.functions.FunctionExp;
import ServiceWorker.service.seviceWorker.functions.TriggerExp;
import lombok.Data;

@Data
public class ServiceWorkerSingle {
	//
	private ServiceWorkerComposite serviceWorkerComposite;
	
	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigSingle config;
	
	private HttpProxyAdaptor httpProxyAdaptor;
	
	private HttpHeaders inputHttpHeaders;
	
	private HttpHeaders outgoingHttpHeaders;
	
	private Map<String, Object> inputQueryParams;
	
	private Map<String, Object> outgoingQueryParams;
	
	private String inputRequestBody;
	
	private String outgoingRequestBody;
	
	private String inputResponseBody;
	
	private ResponseEntity<String> inputResponseEntity;
	
	private String outgoingResponseBody;
	
	private ObjectMapper om = new ObjectMapper();
	
	public ServiceWorkerSingle(String config) {
		//
		this.requestId = UUID.randomUUID().toString();
		try {
			this.config = om.readValue(config, ServiceWorkerConfigSingle.class);
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (JsonProcessingException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public ServiceWorkerSingle(String requestId, String config) {
		//
		this.requestId = requestId;
		try {
			this.config = om.readValue(config, ServiceWorkerConfigSingle.class);
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (JsonProcessingException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
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
	}
	
	public void doWork() {
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
			System.out.println("[ServiceWorkerSingle].executeRequestRule : condition trigger is activated.");
			return true;
		}
		else {
			//System.out.println("[ServiceWorkerSingle].executeRequestRule : condition trigger is not activated. bypass all.");
			//outgoingHttpHeaders = inputHttpHeaders;
			//outgoingQueryParams = inputQueryParams;
			//outgoingRequestBody = inputRequestBody;
			
			System.out.println("[ServiceWorkerSingle].executeRequestRule : any condition trigger is not activated. cut the request.");
			return false;
		}
	}
	
	private void executeResponseRule() {
		// 5)
		if (checkConditionAndExecuteResponseRule()) {
			//System.out.println("[ServiceWorkerSingle].executeResponseRule : condition trigger is activated.");
		}
		else {
			outgoingResponseBody = inputResponseBody;
		}
	}
	
	private Boolean checkConditionAndExecuteRequestRule() {
		//
		List<RequestRule> requestRules = config.getConfig().getRequestRule();
		
		if (!requestRules.isEmpty()) {
			for (RequestRule iterRequestRule : requestRules) {
				if (checkCondition(iterRequestRule.getCondition())) {
					//
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
	
	private Boolean checkCondition(List<String> condition) {
		// 
		Boolean retFlag = true;
		try {
			for (String iter : condition) {
				Boolean tmpFlag = false;
				if (iter.contains("TRIGGER")) {
					tmpFlag = executeTrigger(iter);
				}
				else {
					tmpFlag = false;
				}
				//System.out.println("[ServiceWorkerSingle].checkCondition : tmpFlag = " + tmpFlag);
				retFlag = (retFlag && tmpFlag);
			}
			//System.out.println("[ServiceWorkerSingle].checkCondition : retFlag = " + retFlag);
			return retFlag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Boolean executeTrigger(String trigger) {
		//
		Boolean ret = false;
		
		String tmpTrigger = new String(trigger);
		
		String regExp = "TRIGGER\\((.*?)\\)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(tmpTrigger);
		
		while (m.find()) {
			//
			String matchedExp = m.group();
			
			TriggerExp triggerExp = extractTrigger(matchedExp);
			
			//System.out.println("[ServiceWorkerSingle].executeTrigger : triggerExp = " + triggerExp.toString());
			
			ret = doTrigger(triggerExp);
		}
		
		return ret;
	}
	
	private TriggerExp extractTrigger(String trigger) {
		//
		TriggerExp triggerExp = new TriggerExp();
				
		String tmpTrigger = new String(trigger);
		tmpTrigger = tmpTrigger.replace("TRIGGER", "").replace("(", "").replace(")", "");
		
		Integer index = 0;
		StringTokenizer stringTokenizer = new StringTokenizer(tmpTrigger, ",");
		while(stringTokenizer.hasMoreTokens()){
			triggerExp.setValue(index, stringTokenizer.nextToken().strip());
			index++;
		}
		//System.out.println("[ServiceWorkerSingle].extractTrigger : triggerExp = " + triggerExp.toString());
		return triggerExp;
	}
	
	private Boolean doTrigger(TriggerExp triggerExp) {
		// 
		Boolean ret = false;
		if (triggerExp.getResource().equals("headers")) {
			ret = compareHttpHeaders(triggerExp);
			//System.out.println("[ServiceWorkerSingle].doTrigger : headers = " + ret);
		}
		else if (triggerExp.getResource().equals("queryParams")) {
			ret = compareQueryParams(triggerExp);
			//System.out.println("[ServiceWorkerSingle].doTrigger : queryParams = " + ret);
		}
		else if (triggerExp.getResource().equals("requestBody")) {
			ret = compareRequestBody(triggerExp);
			//System.out.println("[ServiceWorkerSingle].doTrigger : requestBody = " + ret);
		}
		else if (triggerExp.getResource().equals("responseBody")) {
			ret = compareResponseBody(triggerExp);
			//System.out.println("[ServiceWorkerSingle].doTrigger : responseBody = " + ret);
		}
		else if (triggerExp.getResource().equals("responseStatus")) {
			ret = compareResponseStatus(triggerExp);
			//System.out.println("[ServiceWorkerSingle].doTrigger : responseStatus = " + ret);
		}
		else {
			ret = false;
		}
		
		return ret;
	}
	
	private Boolean compareHttpHeaders(TriggerExp triggerExp) {
		//
		List<String> list = extractHeaderValues(triggerExp.getValue());
		return inputHttpHeaders.get(triggerExp.getKey()).containsAll(list);
	}
	
	private List<String> extractHeaderValues(String headerValues) {
		//
		List<String> values = new ArrayList<>();
				
		String tmpTrigger = new String(headerValues);
		tmpTrigger = tmpTrigger.replace("[", "").replace("]", "");
		
		StringTokenizer stringTokenizer = new StringTokenizer(tmpTrigger, ",");
		while(stringTokenizer.hasMoreTokens()){
			values.add(stringTokenizer.nextToken().strip());
		}
		return values;
	}
	
	private Boolean compareQueryParams(TriggerExp triggerExp) {
		//
		try {
			if (inputQueryParams.containsKey(triggerExp.getKey())) {
				if (inputQueryParams.get(triggerExp.getKey()).toString().equals(triggerExp.getValue()))
					return true;
				return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Boolean compareRequestBody(TriggerExp triggerExp) {
		// 
		try {
			Object value = JsonUtil.readValue(inputRequestBody, triggerExp.getKey());
			if (value != null) {
				if(value.toString().equals(triggerExp.getValue()))
					return true;
				else
					return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Boolean compareResponseBody(TriggerExp triggerExp) {
		//
		try {
			Object value = JsonUtil.readValue(inputResponseBody, triggerExp.getKey());
			if (value != null) {
				if(value.toString().equals(triggerExp.getValue()))
					return true;
				else
					return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Boolean compareResponseStatus(TriggerExp triggerExp) {
		//
		try {
			if (inputResponseEntity.getStatusCode().value() == Integer.valueOf(triggerExp.getValue()))
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	private void executeAction(Action action) {
		// 1)
		makeOutgoingHeaders(action);
		
		// 2)
		makeOutgoingQueryParams(action);
		
		// 3)
		makeOutgoingRequestBody(action);
	}
	
	private void makeOutgoingHeaders(Action action) {
		//
		if (outgoingHttpHeaders == null)
			outgoingHttpHeaders = new HttpHeaders();
			
		Map<String, List<String>> headersConfig = action.getHeaders();
		if (!headersConfig.isEmpty()) {
			for (Entry<String, List<String>> iterHeaders : headersConfig.entrySet()) {
				List<String> headerValues = iterHeaders.getValue();
				for (String iterHeaderValue : headerValues) {
					outgoingHttpHeaders.add(iterHeaders.getKey(), (String)executeFunction(iterHeaderValue));
				}
			}
		}
		else {	// byPass
			outgoingHttpHeaders = inputHttpHeaders;
		}
		
	}
	
	private void makeOutgoingQueryParams(Action action) {
		//
		if (outgoingQueryParams == null)
			outgoingQueryParams = new HashMap<>();
		
		Map<String, String> queryParamsConfig = action.getQueryParams();
		if (!queryParamsConfig.isEmpty()) {
			for (Entry<String, String> iterQueryParams : queryParamsConfig.entrySet()) {
				outgoingQueryParams.put(iterQueryParams.getKey(), executeFunction(iterQueryParams.getValue()));
			}
		}
		else {	// byPass
			outgoingQueryParams = inputQueryParams;
		}
	}
	
	private void makeOutgoingRequestBody(Action action) {
		//
		Object requestBodyConfig = action.getRequestBody();
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		JsonUtil.extractJsonKeyPathObjectFormJsonObject(requestBodyConfig, jsonPathKeyObjects);
		
		//System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : b/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
		
		if (!jsonPathKeyObjects.isEmpty()) {
			for (JsonKeyPathObject iter : jsonPathKeyObjects) {
				if (iter.getObject() instanceof String) {
					String exp = (String)iter.getObject();
					if (exp.contains("FUNCTION")) {
						iter.setObject(executeFunction(exp));
					}
				}
			}
		
			//System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : a/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());

			outgoingRequestBody = JsonUtil.mergeJsonString(jsonPathKeyObjects);
		}
		else {
			outgoingRequestBody = inputRequestBody;
		}
		
		//System.out.println("[ServiceWorkerSingle].makeOutgoingRequestBody : outgoingRequetBody = " + outgoingRequestBody);
	}
	
	private ResponseEntity<String> executeHttpProxy() {
		//
		String scheme = config.getConfig().getScheme();
		String url = config.getConfig().getHost();
		String path = config.getConfig().getPath();
		HttpMethod httpMethod = HttpMethod.valueOf(config.getConfig().getMethod());
		
		ResponseEntity<String> result = httpProxyAdaptor.executeHttpProxy(scheme, 
																		  url, 
																		  path, 
																		  outgoingQueryParams, 
																		  httpMethod, 
																		  outgoingHttpHeaders, 
																		  outgoingRequestBody);
		
		inputResponseEntity = result;
		inputResponseBody = result.getBody();
		
		//System.out.println("[ServiceWorkerSingle].executeHttpProxy : inputResponseBody = " + inputResponseBody);
		
		return result;
	}
	
	private Object executeFunction(String str) {
		//
		Object ret = null;
		String tmpStr = new String(str);
		
		String regExp = "FUNCTION\\((.*?)\\)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(tmpStr);

		Boolean isMatched = false;
		while (m.find()) {
			//
			isMatched = true;
			
			String matchedExp = m.group();
			
			//System.out.println("matchedExp = " + matchedExp);
			
			FunctionExp functionExp = extractFunction(matchedExp);
			
			//System.out.println("functionExp = " + functionExp.toString());
			
			ret = doFunction(functionExp);
		}
		
		if (!isMatched) {
			ret = str;
		}
		
		return ret;
	}
	
	private FunctionExp extractFunction(String fuction) {
		//
		FunctionExp functionExp = new FunctionExp();
				
		String tmpFunction = new String(fuction);
		tmpFunction = tmpFunction.replace("FUNCTION", "").replace("(", "").replace(")", "");
		
		Integer index = 0;
		if (tmpFunction.startsWith(",")) {
			index = 1;
		}
		
		StringTokenizer stringTokenizer = new StringTokenizer(tmpFunction, ",");
		while(stringTokenizer.hasMoreTokens()){
			functionExp.setValue(index, stringTokenizer.nextToken().strip());
			index++;
		}
		return functionExp;
	}
	
	private Object doFunction(FunctionExp functionExp) {
		//
		if (functionExp.getApi() == null || functionExp.getApi().isEmpty()) {
			if (functionExp.getResource().equals("headers")) {
				return getValueOfInputHttpHeader(functionExp.getKey());
			}
			else if (functionExp.getResource().equals("queryParams")) {
				return getValueOfInputQueryParams(functionExp.getKey());
			}
			else if (functionExp.getResource().equals("requestBody")) {
				return getValueOfRequestBody(functionExp.getKey());
			}
			else if (functionExp.getResource().equals("responseBody")) {
				return getValueOfResponseBody(functionExp.getKey());
			}
			else {
				return null;
			}
		}
		else {
			if (functionExp.getResource().equals("headers")) {
				return getValueOfInputHttpHeaderFromOhterApi(functionExp.getApi(), functionExp.getKey());
			}
			else if (functionExp.getResource().equals("queryParams")) {
				return getValueOfInputQueryParamsFromOhterApi(functionExp.getApi(), functionExp.getKey());
			}
			else if (functionExp.getResource().equals("requestBody")) {
				return getValueOfRequestBodyFromOhterApi(functionExp.getApi(), functionExp.getKey());
			}
			else if (functionExp.getResource().equals("responseBody")) {
				return getValueOfResponseBodyFromOhterApi(functionExp.getApi(), functionExp.getKey());
			}
			else {
				return null;
			}
		}
	}
	
	private void makeOutgoingResponseBody(Object responseBody) {
		//
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		JsonUtil.extractJsonKeyPathObjectFormJsonObject(responseBody, jsonPathKeyObjects);
		
		//System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : b/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			if (iter.getObject() instanceof String) {
				String exp = (String)iter.getObject();
				if (exp.contains("FUNCTION")) {
					iter.setObject(executeFunction(exp));
				}
			}
		}
		
		//System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : a/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
	
		outgoingResponseBody = JsonUtil.mergeJsonString(jsonPathKeyObjects);
		
		//System.out.println("[ServiceWorkerSingle].makeOutgoingResponseBody : outgoingResponseBody = " + outgoingResponseBody);
	}
	
	// Inner Interface --------------------------------------------------------------------------------------------------
	// Http Header --------------------------
	private String getValueOfInputHttpHeader(String key) {
		// read value of specific key path in inputHttpHeader
		return this.inputHttpHeaders.getFirst(key);
	}
	
	private String getValueOfInputHttpHeaderFromOhterApi(String api, String key) {
		//
		return serviceWorkerComposite.getValueOfInputHttpHeaderFromOhterApi(api, key);
	}
	
	// Query Parameters ---------------------
	private Object getValueOfInputQueryParams(String key) {
		// read value of specific key path in inputHttpHeader
		return this.inputQueryParams.get(key);
	}
	
	private Object getValueOfInputQueryParamsFromOhterApi(String api, String key) {
		//
		return serviceWorkerComposite.getValueOfInputQueryParamsFromOhterApi(api, key);
	}
	
	// Body ---------------------------------
	private Object getValueOfRequestBody(String keyPath) {
		//
		// read value of specific key path in inputRequestBody
		return JsonUtil.readValue(inputRequestBody, keyPath);
	}
	
	private Object getValueOfRequestBodyFromOhterApi(String api, String keyPath) {
		//
		return serviceWorkerComposite.getValueOfRequestBodyFromOhterApi(api, keyPath);
	}
	
	private Object getValueOfResponseBody(String keyPath) {
		//
		// read value of specific key path in inputRequestBody
		return JsonUtil.readValue(inputResponseBody, keyPath);
	}
	
	private Object getValueOfResponseBodyFromOhterApi(String api, String keyPath) {
		//
		return serviceWorkerComposite.getValueOfResponseBodyFromOhterApi(api, keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------

	// Outer Interface --------------------------------------------------------------------------------------------------
	public String getValueOfHeaderByComposite(String key) {
		//
		return getValueOfInputHttpHeader(key);
	}
	
	public Object getValueOfQueryParamByComposite(String key) {
		//
		return getValueOfInputQueryParams(key);
	}
	
	public Object getValueOfRequestBodyByComposite(String keyPath) {
		//
		return getValueOfRequestBody(keyPath);
	}
	
	public Object getValueOfResponseBodyByComposite(String keyPath) {
		//
		return getValueOfResponseBody(keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------
}