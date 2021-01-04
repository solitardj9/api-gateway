package ServiceWorker.model.seviceWorker;

import java.util.Map;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigComposite;

public class ServiceWorkerBox {

	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigComposite config;
	
	private Map<String, ServiceWorker> serviceWorkers;
	
	public Object getValueOfBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		// TODO :
		return serviceWorkers.get(serviceWorkerName).getValueOfBody(keyPath);
	}
}
