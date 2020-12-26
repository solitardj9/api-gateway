package com.solitardj9.apiService.applicationInterface.routeConfiguration.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutesLocatorConfigList {

	private List<RoutesLocatorConfig> configs;
}