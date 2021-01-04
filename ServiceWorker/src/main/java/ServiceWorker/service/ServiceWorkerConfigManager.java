package ServiceWorker.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigBase;

public class ServiceWorkerConfigManager {
	
	private ObjectMapper om = new ObjectMapper();

	private Map<String, ServiceWorkerConfigBase> serviceWorkerConfigs = new ConcurrentHashMap<>();
	
	public void addConfig(String strConfig) {
		//
		try {
			ServiceWorkerConfigBase serviceWorkerConfigBase = om.readValue(strConfig, ServiceWorkerConfigBase.class);
			serviceWorkerConfigs.put(serviceWorkerConfigBase.getName(), serviceWorkerConfigBase);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteConfig(String name) {
		//
		serviceWorkerConfigs.remove(name);
	}
	
	public String getConfigAsString(String name) {
		//
		ServiceWorkerConfigBase serviceWorkerConfigBase = serviceWorkerConfigs.get(name);
		if (serviceWorkerConfigBase != null) {
			try {
				return om.writeValueAsString(serviceWorkerConfigBase);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}
	
	public ServiceWorkerConfigBase getConfig(String name) {
		//
		ServiceWorkerConfigBase serviceWorkerConfigBase = serviceWorkerConfigs.get(name);
		if (serviceWorkerConfigBase != null)
			return serviceWorkerConfigBase;
		else
			return null;
	}
	
	public String getAllConfigs() {
		try {
			return om.writeValueAsString(serviceWorkerConfigs);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}