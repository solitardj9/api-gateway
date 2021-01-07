package com.solitardj9.apiService.applicationInterface.service.service.model;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.solitardj9.apiService.applicationInterface.service.model.Response;
import com.solitardj9.apiService.applicationInterface.service.service.ServiceHandler;

@Service("serviceHandler")
public class ServiceHandlerImpl implements ServiceHandler {

	@Override
	public Response handleService(String service, String b2b, String api, HttpHeaders httpHeaders, String requestBody) {
		// TODO Auto-generated method stub
		return null;
	}
}