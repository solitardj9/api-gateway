package com.solitardj9.apiService.applicationInterface.routeConfiguration.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteConfigStatus {

	private Map<String, RouteConfig> routeConfigMap;
	
	private RoutesLocatorConfigList routesLocatorConfigList;
}