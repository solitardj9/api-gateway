package com.solitardj9.apiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscoveryApplication.class, args);
		
		logger.info("[ServiceDiscoveryApplication].main = starts");
	}
}