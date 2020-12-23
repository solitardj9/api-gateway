package com.solitardj9.apiService.serviceInterface.filters.routeFilterInterface;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceRoutingFilterInterface {
	//
	private static final Logger logger = LoggerFactory.getLogger(ServiceRoutingFilterInterface.class);
	
	@Autowired
	RouteDefinitionLocator locator;
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		
		//return builder.routes().route(r -> r.path("/**").and().header("x-api-service", "serviceA").uri("lb://service-a")).route(r -> r.path("/**").and().header("x-api-service", "serviceB").uri("lb://service-b")).build();
		
		Builder myBuilder = builder.routes();
		myBuilder = myBuilder.route(
			//r -> r.path("/**").and().header("x-api-service", "serviceA").uri("lb://service-a")
			r -> r.path("/service-a/**").uri("lb://service-a")
		);
		myBuilder = myBuilder.route(
			//r -> r.path("/**").and().header("x-api-service", "serviceB").uri("lb://service-b")
			r -> r.path("/service-b/**").uri("lb://service-b")
		);
		myBuilder = myBuilder.route(
			r -> r.path("/v3/api-docs/service-a")
				//.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v2/api-docs"))
			.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/v2/api-docs"))
				.uri("lb://service-a")
		);
		myBuilder = myBuilder.route(
			r -> r.path("/v3/api-docs/service-b")
			   //.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v2/api-docs"))
			.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/v2/api-docs"))
			   .uri("lb://service-b")
		);
		
		return myBuilder.build();
	}
	
	@Bean
	public List<GroupedOpenApi> apis() {
	    List<GroupedOpenApi> groups = new ArrayList<>();
	    List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
	    
//	    definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//	        String name = routeDefinition.getId().replaceAll("-service", "");
//	        GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").setGroup(name).build();
//	    });
	    
	    GroupedOpenApi groupedOpenApi_a = GroupedOpenApi.builder().pathsToMatch("/service-a/**").setGroup("service-a").build();
	    groups.add(groupedOpenApi_a);
	    GroupedOpenApi groupedOpenApi_b = GroupedOpenApi.builder().pathsToMatch("/service-b/**").setGroup("service-b").build();
	    groups.add(groupedOpenApi_b);
	    
	    return groups;
	}
}