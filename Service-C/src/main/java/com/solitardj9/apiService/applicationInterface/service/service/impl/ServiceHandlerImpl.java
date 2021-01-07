package com.solitardj9.apiService.applicationInterface.service.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.solitardj9.apiService.applicationInterface.service.model.Response;
import com.solitardj9.apiService.applicationInterface.service.service.ServiceHandler;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.ServiceWorkerConfigManager;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.ServiceWorkerConfigBase;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.ServiceWorkerConfigSingle;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.task.ServiceWorkerSingle;

@Service("serviceHandler")
public class ServiceHandlerImpl implements ServiceHandler {

	@Value("${serviceWorker.config}")
	private String CONFIG;
	
	private ServiceWorkerConfigManager serviceWorkerConfigManager;
	
	@PostConstruct
	public void init() {
		serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		serviceWorkerConfigManager.addConfig(CONFIG);
	}
	
	@Override
	public Response handleService(String service, String b2b, String api, HttpHeaders httpHeaders, String requestBody) {
		// 
		ServiceWorkerConfigBase serviceWorkerConfigBase = serviceWorkerConfigManager.getConfig(api);
		
		if (serviceWorkerConfigBase instanceof ServiceWorkerConfigSingle) {
			ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(serviceWorkerConfigManager.getConfigAsString(api));
			serviceWorkerSingle.setInputs(httpHeaders, null, requestBody);
			
			serviceWorkerSingle.doWork();
			
			Response response = new Response(serviceWorkerSingle.getInputResponseEntity().getStatusCode(),
											 serviceWorkerSingle.getOutgoingResponseBody());
			
			return response;
		}
		else {
			// TODO : 
			return null;
		}
	}
}