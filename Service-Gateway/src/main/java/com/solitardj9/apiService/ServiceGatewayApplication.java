package com.solitardj9.apiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceGatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServiceGatewayApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceGatewayApplication.class, args);
		
		logger.info("[ServiceGatewayApplication].main = starts");
	}
}