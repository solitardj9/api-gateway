package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use= JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
    @Type(value = ServiceWorkerConfigComposite.class, name = "composite"),
    @Type(value = ServiceWorkerConfigSingle.class, name = "single")
})
public class ServiceWorkerConfigBase {
	//
	private String name;
	
	@JsonProperty(value = "type") private String type;
}