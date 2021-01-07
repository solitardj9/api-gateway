package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.task;

import java.util.Map;

import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.ServiceWorkerConfigComposite;

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