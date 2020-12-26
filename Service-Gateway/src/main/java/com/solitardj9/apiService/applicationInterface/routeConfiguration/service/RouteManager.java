package com.solitardj9.apiService.applicationInterface.routeConfiguration.service;

public interface RouteManager {
	
	public void stopRoute(String serviceName);

    public void startRoute(String serviceName);
    
    public String getRoutes();    
}