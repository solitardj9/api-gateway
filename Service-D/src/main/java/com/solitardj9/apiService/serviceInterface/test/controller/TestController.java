package com.solitardj9.apiService.serviceInterface.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.apiService.serviceInterface.test.model.common.Response;

@RestController
@RequestMapping(value="/service/service-a/b2b/test/api")
@CrossOrigin(origins = "*")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/check")
	public ResponseEntity onPutCheck(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[TestController].onPutCheck(/service-a/test/check) is called.");
		
		return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "This is result of API Test on Service-A"), HttpStatus.OK);
    }
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/broadcast")
	public ResponseEntity onPutBroadcast(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[TestController].onPutBroadcast(/service-a/test/broadcast) is called.");
		
		return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "This is result of API Test on Service-A"), HttpStatus.OK);
    }
}