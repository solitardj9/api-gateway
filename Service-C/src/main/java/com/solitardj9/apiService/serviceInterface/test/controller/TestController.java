package com.solitardj9.apiService.serviceInterface.test.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.apiService.serviceInterface.test.model.common.Response;
import com.solitardj9.apiService.serviceInterface.test.model.list.MyListRequest;
import com.solitardj9.apiService.serviceInterface.test.model.list.MyListResoponse;
import com.solitardj9.apiService.serviceInterface.test.model.list.MyResoponse;

@RestController
@RequestMapping(value="/service/service-a/b2b/test/api")
@CrossOrigin(origins = "*")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	private ObjectMapper om = new ObjectMapper();

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
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/getList")
	public ResponseEntity onGetList(@RequestHeader HttpHeaders header, 
			@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TestController].onGetList is called.");
		
		logger.info("Header = " + header.toString());
		String locationInfo = header.getFirst("x-location-info");
		try {
			Map<String, Object> locationInfoMap = om.readValue(locationInfo, Map.class);
			System.out.println("locationInfoMap = " + locationInfoMap.toString());
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		MyListRequest myListRequest = null;
		try {
			myListRequest = om.readValue(requestBody, MyListRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		logger.info("requestBody = " + myListRequest.toString());
		
		MyListResoponse myListResoponse = new MyListResoponse("service-a", "onGetList result");
		MyResoponse myResoponse = new MyResoponse(HttpStatus.OK.value(), "success", myListResoponse);
		
		return new ResponseEntity<>(myResoponse, HttpStatus.OK);
    }
}