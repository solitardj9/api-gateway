package ServiceWorker.model.seviceWorker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;

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
	
	private HttpHeaders outputHttpHeaders;
	
	private Map<String, Object> inputQueryParams;
	
	private Map<String, Object> outputQueryParams;
	
	private Object outputBody;
	
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
	
	public void setInputs(HttpHeaders inputHttpHeaders, Map<String, Object> inputQueryParams) {
		//
		this.inputHttpHeaders = inputHttpHeaders;
		this.inputQueryParams = inputQueryParams;
	}
	
	public void doWork() {
		// TODO :
		// 1)
		makeOutputHeaders();
		
		// 2)
		makeOutputQueryParams();
		
		// 3)
		executeHttpProxy();
		
		// 4) 
		makeOutputBody();
	}
	
	private void makeOutputHeaders() {
		//
		if (outputHttpHeaders == null)
			outputHttpHeaders = new HttpHeaders();
			
		Map<String, List<String>> headers = this.config.getConfig().getHeaders();
		for (Entry<String, List<String>> iterHeaders : headers.entrySet()) {
			List<String> headerValues = iterHeaders.getValue();
			for (String iterHeaderValue : headerValues) {
				outputHttpHeaders.add(iterHeaders.getKey(), (String)executeFunction(iterHeaderValue));
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	private void makeOutputQueryParams() {
		// TODO :
	}
	
	private void executeHttpProxy() {
		// TODO :
		String scheme = config.getConfig().getScheme();
		//httpProxyAdaptor.executeHttpProxy(scheme, url, path, queryParams, method, headers, body)
	}
	
	private void makeOutputBody() {
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
			else if (functionExp.getResource().equals("body")) {
				return getValueOfBodyFromReponse(functionExp.getKey());
			}
			else {
				return null;
			}
		}
		else {
			if (functionExp.getResource().equals("headers")) {
				return getValueOfInputHttpHeaderFromOhterApi(functionExp.getApi(), functionExp.getKey());
			}
			else if (functionExp.getResource().equals("body")) {
				return getValueOfBodyFromOhterApi(functionExp.getApi(), functionExp.getKey());
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
	
	
	
	
	
	
	// Body ---------------------------------
	private Object getValueOfBodyFromReponse(String keyPath) {
		// read value of specific key path in outputBody
		return JsonUtil.readValue(outputBody, keyPath);
	}
	
	private Object getValueOfBodyFromOhterApi(String api, String keyPath) {
		//
		return serviceWorkerComposite.getValueOfBodyFromOhterApi(api, keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------

	// Outer Interface --------------------------------------------------------------------------------------------------
	public String getValueOfHeader(String key) {
		//
		return getValueOfInputHttpHeader(key);
	}
	
	public Object getValueOfBody(String keyPath) {
		//
		return getValueOfBodyFromReponse(keyPath);
	}
	//-------------------------------------------------------------------------------------------------------------------
}