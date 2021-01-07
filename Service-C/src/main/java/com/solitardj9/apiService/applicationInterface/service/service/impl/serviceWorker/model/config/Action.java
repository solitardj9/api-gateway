package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {
	
	private Map<String, List<String>> headers;
	
	private Map<String, String> queryParams;
	
	private Object requestBody;
}