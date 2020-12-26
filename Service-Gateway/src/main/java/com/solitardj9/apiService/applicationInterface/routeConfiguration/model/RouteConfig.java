package com.solitardj9.apiService.applicationInterface.routeConfiguration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteConfig {

    private Boolean enable;         // true / false

    private String serviceName;		// service-a

    private String path;			// /service-a/**

    private String routingUri;		// http://localhost:19370

    private String openApiPath;		// /v3/api-docs/(?<path>.*)

    private String swaggerPath;		// /v2/api-docs
}