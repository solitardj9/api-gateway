package com.solitardj9.apiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.solitardj9.apiService.applicationInterface.test.broadcaster.Broadcaster;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceGatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(ServiceGatewayApplication.class);
	
	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = SpringApplication.run(ServiceGatewayApplication.class, args);
		
		logger.info("[ServiceGatewayApplication].main = starts");
		
		Broadcaster broadcaster = ((Broadcaster)context.getBean("broadcaster"));
		broadcaster.doTest();
	}
}