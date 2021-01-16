package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.singleConfig;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
	
	private String path;
	
	private Map<String, List<String>> headers;
	
	private Map<String, Object> queryParams;
	
	private Object requestBody;
}