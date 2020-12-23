package com.solitardj9.apiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceCApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceCApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ServiceCApplication.class, args);
		
		logger.info("[ServiceCApplication].main = starts");
	}
}