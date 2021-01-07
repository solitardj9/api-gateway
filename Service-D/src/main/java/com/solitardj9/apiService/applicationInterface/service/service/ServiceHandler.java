package com.solitardj9.apiService.applicationInterface.service.service;

import org.springframework.http.HttpHeaders;

import com.solitardj9.apiService.applicationInterface.service.model.Response;

public interface ServiceHandler {
	
	public Response handleService(String service, String b2b, String api, HttpHeaders httpHeaders, String requestBody); 
}