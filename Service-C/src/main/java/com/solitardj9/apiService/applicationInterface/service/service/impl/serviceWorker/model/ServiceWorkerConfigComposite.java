package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonTypeName("composite")
public class ServiceWorkerConfigComposite extends ServiceWorkerConfigBase {
	//
	private List<String> sequence;
}