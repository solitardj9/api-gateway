package ServiceWorker.model.seviceWorker;

import java.util.Map;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigComposite;

public class ServiceWorkerBox {

	private String requestId;		// ���� �� log �� ID
	
	private ServiceWorkerConfigComposite config;
	
	private Map<String, ServiceWorker> serviceWorkers;
	
	public Object getValueOfBodyFromOhterApi(String serviceWorkerName, String keyPath) {
		// TODO :
		return serviceWorkers.get(serviceWorkerName).getValueOfBody(keyPath);
	}
}
