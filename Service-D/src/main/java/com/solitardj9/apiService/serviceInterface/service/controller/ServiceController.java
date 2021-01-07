package com.solitardj9.apiService.serviceInterface.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.apiService.applicationInterface.service.model.Response;
import com.solitardj9.apiService.applicationInterface.service.service.ServiceHandler;

@RestController
@RequestMapping(value="/service/service-b/b2b")
@CrossOrigin(origins = "*")
public class ServiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
	@Autowired
	ServiceHandler serviceHandler;

	@SuppressWarnings("rawtypes")
	@PutMapping(value="/{b2b}/api/{api}")
	public ResponseEntity onPutService(@PathVariable("b2b") String b2b,
									   @PathVariable("api") String api,
									   @RequestHeader HttpHeaders headers,
									   @RequestBody(required=false) String requestBody) {
		//
		logger.info("[ServiceController].onPutService(Service = service-b, b2b = " + b2b + ", api = " + api + ") is called.");
		
		Response response = serviceHandler.handleService("service-b", b2b, api, headers, requestBody);
		
		return new ResponseEntity<>(response.getResponseBody(), response.getHttpStatus());
    }
}