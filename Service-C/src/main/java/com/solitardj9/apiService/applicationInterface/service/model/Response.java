package com.solitardj9.apiService.applicationInterface.service.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	private HttpStatus httpStatus;
	
	private String responseBody;
}