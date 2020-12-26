package com.solitardj9.apiService.serviceInterface.filters.routeFilterModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiConfig {
	
	private String serviceName;		// service-a
	
	private String path;			// /service-a/**
	
	private String routingUri;		// http://localhost:19370
	
	private String openApiPath;		// /v3/api-docs/(?<path>.*)
	
	private String swaggerPath;		// /v2/api-docs
}