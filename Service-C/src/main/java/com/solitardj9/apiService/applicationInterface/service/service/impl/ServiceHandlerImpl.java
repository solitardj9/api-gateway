package com.solitardj9.apiService.applicationInterface.service.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.solitardj9.apiService.applicationInterface.service.model.Response;
import com.solitardj9.apiService.applicationInterface.service.service.ServiceHandler;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.ServiceWorkerConfigBase;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.ServiceWorkerConfigComposite;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.ServiceWorkerConfigSingle;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.worker.ServiceWorkerComposite;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.service.ServiceWorkerConfigManager;

@Service("serviceHandler")
public class ServiceHandlerImpl implements ServiceHandler {
	
	@Value("${serviceWorker.config.accuWeather.composite}")
	private String CONFIG_ACCU_WEATHER_COMPOSITE;
	
	@Value("${serviceWorker.config.accuWeather.singleLocation}")
	private String CONFIG_ACCU_WEATHER_LOCATION;
	
	@Value("${serviceWorker.config.accuWeather.singleForecastDaily}")
	private String CONFIG_ACCU_WEATHER_FORECAST_DAILY;
	
	private ServiceWorkerConfigManager serviceWorkerConfigManager;
	
	@PostConstruct
	public void init() {
		serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		
		serviceWorkerConfigManager.addConfig(CONFIG_ACCU_WEATHER_COMPOSITE);
		serviceWorkerConfigManager.addConfig(CONFIG_ACCU_WEATHER_LOCATION);
		serviceWorkerConfigManager.addConfig(CONFIG_ACCU_WEATHER_FORECAST_DAILY);
	}
	
	@Override
	public Response handleService(String service, String b2b, String api, HttpHeaders httpHeaders, String requestBody) {
		//
		ServiceWorkerConfigBase serviceWorkerConfigBase = serviceWorkerConfigManager.getConfig("API-ACCU-WEATHER-V1-FORECAST_DAILY_COMPOSITE");
		if (serviceWorkerConfigBase.getType().equals("composite")) {
			//
			ServiceWorkerConfigComposite serviceWorkerConfigComposite = (ServiceWorkerConfigComposite)serviceWorkerConfigBase;
			
			ServiceWorkerComposite serviceWorker = new ServiceWorkerComposite();
			serviceWorker.addServiceWorkerConfigComposite(serviceWorkerConfigComposite);
			
			Integer index = 0;
			for (String iter : serviceWorkerConfigComposite.getSequence()) {
				serviceWorker.addServiceWorkerConfigSingle(index, (ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig(iter));
				index++;
			}
			
			serviceWorker.setInputs(httpHeaders, null, requestBody);
			
			System.out.println("//---------------------------------------------");
			System.out.println("InputHttpHeaders = " + serviceWorker.getInputHttpHeaders());
			System.out.println("InputQueryParams = " + serviceWorker.getInputQueryParams());
			System.out.println("InputRequestBody = " + serviceWorker.getInputRequestBody());
			System.out.println("DcInputRequestBody = " + serviceWorker.getDcInputRequestBody().jsonString());
			System.out.println("//---------------------------------------------");
			
			serviceWorker.executeWork();
			
			System.out.println("//---------------------------------------------");
			System.out.println("OutgoingRequestBody = " + serviceWorker.getOutgoingRequestBody());
			System.out.println("//---------------------------------------------");
			
			Response response = new Response(serviceWorker.getOutgoingHttpStatus(),
											 serviceWorker.getOutgoingRequestBody());
			
			return response;
		}
		else if (serviceWorkerConfigBase.getType().equals("single")) {
			// TODO : 
			return null;
		}
		else
			return null;
	}
}