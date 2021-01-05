package ServiceWorker.model.seviceWorker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigSingle;
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
	
	private String outgoingResponseBody;
	
	private ObjectMapper om = new ObjectMapper();
	
	public ServiceWorkerSingle(String config) {
		//
		try {
			this.config = om.readValue(config, ServiceWorkerConfigSingle.class);
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (JsonProcessingException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputs(HttpHeaders inputHttpHeaders, Map<String, Object> inputQueryParams, String inputRequestBody) {
		//
		this.inputHttpHeaders = inputHttpHeaders;
		this.inputQueryParams = inputQueryParams;
		this.inputRequestBody = inputRequestBody;
	}
	
	public void doWork() {
		// TODO :
		// 1)
		makeOutgoingHeaders();
		
		// 2)
		makeOutgoingQueryParams();
		
		// 3)
		makeOutgoingRequestBody();
		
		// 4)
		executeHttpProxy();
		
		// 5) 
		makeOutgoingResponseBody();
	}
	
	private void makeOutgoingHeaders() {
		//
		if (outgoingHttpHeaders == null)
			outgoingHttpHeaders = new HttpHeaders();
			
		Map<String, List<String>> headersConfig = this.config.getConfig().getHeaders();
		for (Entry<String, List<String>> iterHeaders : headersConfig.entrySet()) {
			List<String> headerValues = iterHeaders.getValue();
			for (String iterHeaderValue : headerValues) {
				outgoingHttpHeaders.add(iterHeaders.getKey(), (String)executeFunction(iterHeaderValue));
			}
		}
	}
	
	private void makeOutgoingQueryParams() {
		//
		if (outgoingQueryParams == null)
			outgoingQueryParams = new HashMap<>();
		
		Map<String, String> queryParamsConfig = this.config.getConfig().getQueryParams();
		for (Entry<String, String> iterQueryParams : queryParamsConfig.entrySet()) {
			outgoingQueryParams.put(iterQueryParams.getKey(), executeFunction(iterQueryParams.getValue()));
		}
	}
	
	private void makeOutgoingRequestBody() {
		// TODO : 
		Object requestBodyConfig = this.config.getConfig().getRequestBody();
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		JsonUtil.extractJsonKeyPathObjectFormJsonObject(requestBodyConfig, jsonPathKeyObjects);
		
		System.out.println("b/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			if (iter.getObject() instanceof String) {
				String exp = (String)iter.getObject();
				if (exp.contains("FUNCTION")) {
					iter.setObject(executeFunction(exp));
				}
			}
		}
		
		System.out.println("a/f jsonPathKeyObjects = " + jsonPathKeyObjects.toString());

		outgoingRequestBody = JsonUtil.mergeJsonString(jsonPathKeyObjects);
		
		System.out.println("outgoingRequetBody = " + outgoingRequestBody);
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
		
		inputResponseBody = result.getBody();
		
		//System.out.println("inputResponseBody = " + inputResponseBody);
		
		return result;
	}
	
	private void makeOutgoingResponseBody() {
		// TODO :
	}
	
	
	
	
	
	
	private Object executeFunction(String str) {
		//
		Object ret = null;
		String tmpStr = new String(str);
		
		String regExp = "FUNCTION\\((.*?)\\)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(tmpStr);
		
		while (m.find()) {
			//
			String matchedExp = m.group();
			
			System.out.println("matchedExp = " + matchedExp);
			
			FunctionExp functionExp = extractFunction(matchedExp);
			System.out.println("functionExp = " + functionExp.toString());
			
			ret = doFunction(functionExp);
		}
		
		return ret;
	}
	
	private FunctionExp extractFunction(String fuction) {
		//
		FunctionExp functionExp = new FunctionExp();
				
		String tmpFunction = new String(fuction);
		tmpFunction = tmpFunction.replace("FUNCTION", "").replace("(", "").replace(")", "").replace(" ", "");
		
		Integer index = 0;
		if (tmpFunction.startsWith(",")) {
			index = 1;
		}
		
		StringTokenizer stringTokenizer = new StringTokenizer(tmpFunction, ",");
		while(stringTokenizer.hasMoreTokens()){
			functionExp.setValue(index, stringTokenizer.nextToken());
			index++;
		}
		return functionExp;
	}
	
	private Object doFunction(FunctionExp functionExp) {
		// TODO : 
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
			else {
				return null;
			}
		}
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
		// TODO : 
		// read value of specific key path in inputRequestBody
		return JsonUtil.readValue(inputRequestBody, keyPath);
	}
	
	private Object getValueOfRequestBodyFromOhterApi(String api, String keyPath) {
		// TODO : 
		return serviceWorkerComposite.getValueOfRequestBodyFromOhterApi(api, keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------

	// Outer Interface --------------------------------------------------------------------------------------------------
	public String getValueOfHeader(String key) {
		//
		return getValueOfInputHttpHeader(key);
	}
	
	public Object getValueOfQueryParam(String key) {
		//
		return getValueOfInputQueryParams(key);
	}
	
	public Object getValueOfBody(String keyPath) {
		// TODO : 
		return getValueOfRequestBody(keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------
}