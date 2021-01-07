package ServiceWorker.service.seviceWorker;

import java.util.Map;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigComposite;

public class ServiceWorkerComposite {

	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigComposite config;
	
	private Map<String, ServiceWorkerSingle> serviceWorkers;
	
	public String getValueOfInputHttpHeaderFromOhterApi(String serviceWorkerName, String key) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfHeaderByComposite(key);
	}
	
	public Object getValueOfInputQueryParamsFromOhterApi(String serviceWorkerName, String key) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfQueryParamByComposite(key);
	}
	
	public Object getValueOfRequestBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfRequestBodyByComposite(keyPath);
	}
	
	public Object getValueOfResponseBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfResponseBodyByComposite(keyPath);
	}
	
	public Object getValueOfResponseStatusFromOhterApi(String serviceWorkerName, String key) {
		//
		return serviceWorkers.get(serviceWorkerName).getValueOfResponseStatusByComposite(key);
	}
}