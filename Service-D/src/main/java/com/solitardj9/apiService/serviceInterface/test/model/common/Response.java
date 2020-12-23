package com.solitardj9.apiService.serviceInterface.test.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
	//
	private Integer code;
	
	private String message;
}