package com.solitardj9.apiService.applicationInterface.routeConfiguration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutesLocatorConfig {
	
	private String id;
	
    private String uri;
    
    private String predicates;
    
    private String filters;
    
    private String metadata;
    
    private String order;
}