package ServiceWorker.model.seviceWorker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigSingle;
import lombok.Data;

@Data
public class ServiceWorker {
	//
	private ServiceWorkerBox serviceWorkerBox;
	
	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigSingle config;
	
	private HttpProxyAdaptor httpProxyAdaptor;
	
	private HttpHeaders inputHttpHeaders;
	
	private HttpHeaders outputHttpHeaders;
	
	private Map<String, Object> inputQueryParams;
	
	private Map<String, Object> outputQueryParams;
	
	private Object outputBody;
	
	private ObjectMapper om = new ObjectMapper();
	
	public ServiceWorker(String config) {
		//
		try {
			this.config = om.readValue(config, ServiceWorkerConfigSingle.class);
			this.httpProxyAdaptor = new HttpProxyAdaptor();
		} catch (JsonProcessingException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void setInputs(HttpHeaders inputHttpHeaders, Map<String, Object> inputQueryParams) {
		// TODO :
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
		// TODO :
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
	
	private Object getValueOfBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		// TODO :
		// read value of specific key path in outputBody
		return serviceWorkerBox.getValueOfBodyFromOhterApi(serviceWorkerName, keyPath);
	}
	
	private Object getValueOfBodyFromReponse(String keyPath) {
		// TODO :
		// read value of specific key path in outputBody
		return JsonUtil.readValue(outputBody, keyPath);
	}
	
	public Object getValueOfBody(String keyPath) {
		// TODO :
		return getValueOfBodyFromReponse(keyPath);
	}
}