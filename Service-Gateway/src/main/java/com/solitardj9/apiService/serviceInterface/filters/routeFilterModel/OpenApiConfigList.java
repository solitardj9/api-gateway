package com.solitardj9.apiService.serviceInterface.filters.routeFilterModel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiConfigList {
	
	private List<OpenApiConfig> configs;
}