package com.solitardj9.apiService.applicationInterface.routeConfiguration.service.impl;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RouteConfig;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RouteConfigList;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RouteConfigStatus;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RoutesLocatorConfig;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RoutesLocatorConfigList;
import com.solitardj9.apiService.applicationInterface.routeConfiguration.service.RouteManager;
import com.solitardj9.apiService.applicationInterface.routeFilters.service.RouteFilterManager;

import reactor.core.publisher.Flux;

@Service("routeManager")
public class RouteManagerImpl implements RouteManager {

    private static final Logger logger = LogManager.getLogger(RouteManagerImpl.class);

    @Autowired
    private RefreshableRoutesLocator refreshableRoutesLocator;

    @Autowired
    RouteFilterManager routeFilterManager;

    @Value("${route.config}")
	private String routeConfig;

    private Map<String, RouteConfig> routeConfigMap = new HashMap<>();

    private RouteConfigList routeConfigList;

    private ObjectMapper om = new ObjectMapper();

    @PostConstruct
    public void init() {

        setRouteFilterConfigList(routeConfig);

        buildRoutes();
    }

    @Override
    public void stopRoute(String serviceName) {

        routeConfigMap.get(serviceName).setEnable(false);

        buildRoutes();
    }

    @Override
    public void startRoute(String serviceName) {

        routeConfigMap.get(serviceName).setEnable(true);

        buildRoutes();
    }

    private void setRouteFilterConfigList(String routeConfig) {

        try {
            routeConfigList = om.readValue(routeConfig, RouteConfigList.class);

            for (RouteConfig iter : routeConfigList.getConfigs()) {
                routeConfigMap.put(iter.getServiceName(), iter);
            }
        } catch (JsonProcessingException e) {
            logger.error("[RouteManager].setRouteFilterConfigList : error = " + e);
        }
    }

    private List<RouteConfig> getEnabledRouteFilterConfigList() {

        List<RouteConfig> configs = new ArrayList<>();

        for (Map.Entry<String, RouteConfig> iter : routeConfigMap.entrySet()) {

            if (iter.getValue().getEnable().equals(true)) {
                configs.add(iter.getValue());
            }
        }

        return configs;
    }

    private void buildRoutes() {

        logger.info("[RouteManager].buildRoutes : building routes = starts.");

        refreshableRoutesLocator.clearRoutes();

        List<RouteConfig> configs = getEnabledRouteFilterConfigList();

        try {
            refreshableRoutesLocator.addRoute(configs, routeFilterManager);
            refreshableRoutesLocator.buildRoutes();
        } catch (URISyntaxException e) {
            logger.error("[RouteManager].setRouteFilterConfigList : error = " + e);
        }

        logger.info("[RouteManager].buildRoutes : building routes = ends.");
    }
    
    public String getRoutes() {
    	
    	Flux<Route> routes = refreshableRoutesLocator.getRoutes();
    	
    	List<RoutesLocatorConfig> routesLocatorConfigs = new ArrayList<>();
//    	for (Map.Entry<String, RouteConfig> iter : routeConfigMap.entrySet()) {
//    		
//    		Route route = routes.filter(r -> r.getId().equals(iter.getKey())).next().block();
//    		if (route != null) {
//    			
//    			RoutesLocatorConfig routesLocatorConfig = new RoutesLocatorConfig();
//    			
//    			routesLocatorConfig.setId(route.getId());
//    			routesLocatorConfig.setUri(route.getUri().toString());
//    			routesLocatorConfig.setPredicates(route.getPredicate().toString());
//    			routesLocatorConfig.setFilters(route.getFilters().toString());
//    			routesLocatorConfig.setMetadata(route.getMetadata().toString());
//    			routesLocatorConfig.setOrder(String.valueOf(route.getOrder()));
//                
//    			routesLocatorConfigs.add(routesLocatorConfig);
//    		}
//        }
    	
    	RouteConfigStatus routeConfigStatus = new RouteConfigStatus(routeConfigMap, new RoutesLocatorConfigList(routesLocatorConfigs)); 
    	
    	String strRouteConfigStatus = null;
    	try {
    		strRouteConfigStatus = om.writeValueAsString(routeConfigStatus);
		} catch (JsonProcessingException e) {
			logger.error("[RouteManager].getRoutes : error = " + e);
		}
    	
    	return strRouteConfigStatus;
    }
}
