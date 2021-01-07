package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonTypeName("single")
public class ServiceWorkerConfigSingle extends ServiceWorkerConfigBase {
	//
	private Config config;
}