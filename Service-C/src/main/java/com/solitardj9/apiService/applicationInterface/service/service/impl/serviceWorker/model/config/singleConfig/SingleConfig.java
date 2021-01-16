package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.singleConfig;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleConfig {
	
	private String scheme;
	
	private String host; 
	
	private String method;
	
	private List<RequestRule> requestRule;
	
	private List<ResponseRule> responseRule;
}