package ServiceWorker.model.seviceWorker;

import java.util.Map;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigComposite;

public class ServiceWorkerComposite {

	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigComposite config;
	
	private Map<String, ServiceWorkerSingle> serviceWorkers;
	
	public String getValueOfInputHttpHeaderFromOhterApi(String serviceWorkerName, String key) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfHeader(key);
	}
	
	public Object getValueOfInputQueryParamsFromOhterApi(String serviceWorkerName, String key) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfQueryParam(key);
	}
	
	public Object getValueOfRequestBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfBody(keyPath);
	}
	
	
}
