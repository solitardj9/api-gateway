package com.solitardj9.apiService.applicationInterface.routeConfiguration.service.impl;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.solitardj9.apiService.serviceInterface.filters.routeFilterModel.OpenApiConfig;
//import com.solitardj9.apiService.serviceInterface.filters.routeFilterModel.OpenApiConfigList;

@Deprecated
//@Configuration
public class ServiceRoutingFilterInterface {
	//
//	private static final Logger logger = LoggerFactory.getLogger(ServiceRoutingFilterInterface.class);
//	
//	@Value("${openapi.config.list}")
//	private String strOpenApiConfigList;
//	
//	@Autowired
//	RouteDefinitionLocator locator;
//	
//	private ObjectMapper om = new ObjectMapper();
//	
//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		
//		OpenApiConfigList openApiConfigList = null;
//		try {
//			openApiConfigList = om.readValue(strOpenApiConfigList, OpenApiConfigList.class);
//		} catch (JsonProcessingException e) {
//			logger.error("[ServiceRoutingFilterInterface].customRouteLocator : error = " + e);
//		}
//		//System.out.println("openApiConfigList = " + openApiConfigList.toString());
//		
//		if (openApiConfigList != null) {
//			//
//			Builder myBuilder = builder.routes();
//			for (OpenApiConfig config : openApiConfigList.getConfigs()) {
//				//
//				myBuilder = myBuilder.route(
//						r -> r.path(config.getPath()).uri(config.getRoutingUri())
//				);
//				
//				myBuilder = myBuilder.route(
//						r -> r.path(config.getOpenApiPath())
//							  .filters(f -> f.rewritePath(config.getOpenApiPath(), config.getSwaggerPath()))
//							  .uri(config.getRoutingUri())
//				);
//			}
//			
//			return myBuilder.build();
//		}
//		else {
//			logger.error("[ServiceRoutingFilterInterface].customRouteLocator : error = openApiConfigList is null.");
//			
//			return builder.routes().build();
//		}
//		
//		
//		
//		//return builder.routes().route(r -> r.path("/**").and().header("x-api-service", "serviceA").uri("lb://service-a")).route(r -> r.path("/**").and().header("x-api-service", "serviceB").uri("lb://service-b")).build();
//		
////		Builder myBuilder = builder.routes();
////		myBuilder = myBuilder.route(
////			//r -> r.path("/**").and().header("x-api-service", "serviceA").uri("lb://service-a")
////			//r -> r.path("/service-a/**").uri("lb://service-a")
////			r -> r.path("/service-a/**").uri("http://localhost:19370")
////		);
////		myBuilder = myBuilder.route(
////			//r -> r.path("/**").and().header("x-api-service", "serviceB").uri("lb://service-b")
////			//r -> r.path("/service-b/**").uri("lb://service-b")
////			r -> r.path("/service-b/**").uri("http://localhost:19371")
////		);
////		myBuilder = myBuilder.route(
////			r -> r.path("/v3/api-docs/service-a")
////				//.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v2/api-docs"))
////				.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/v2/api-docs"))
////				//.uri("lb://service-a")
////				.uri("http://localhost:19370")
////		);
////		myBuilder = myBuilder.route(
////			r -> r.path("/v3/api-docs/service-b")
////				//.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v2/api-docs"))
////				.filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/v2/api-docs"))
////				//.uri("lb://service-b")
////				.uri("http://localhost:19371")
////		);
////		
////		return myBuilder.build();
//	}
//	
////	@Bean
////	public List<GroupedOpenApi> apis() {
////	    List<GroupedOpenApi> groups = new ArrayList<>();
////	    List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
////	    
//////	    definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//////	        String name = routeDefinition.getId().replaceAll("-service", "");
//////	        GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").setGroup(name).build();
//////	    });
////	    
////	    GroupedOpenApi groupedOpenApi_a = GroupedOpenApi.builder().pathsToMatch("/service-a/**").setGroup("service-a").build();
////	    groups.add(groupedOpenApi_a);
////	    GroupedOpenApi groupedOpenApi_b = GroupedOpenApi.builder().pathsToMatch("/service-b/**").setGroup("service-b").build();
////	    groups.add(groupedOpenApi_b);
////	    
////	    return groups;
////	}
}