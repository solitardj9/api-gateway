package com.solitardj9.apiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceDApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceDApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceDApplication.class, args);
		
		logger.info("[ServiceDApplication].main = starts");
	}
}