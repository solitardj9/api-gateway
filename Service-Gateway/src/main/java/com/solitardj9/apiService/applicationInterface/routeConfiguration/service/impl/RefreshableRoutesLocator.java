package com.solitardj9.apiService.applicationInterface.routeConfiguration.service.impl;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.solitardj9.apiService.applicationInterface.routeConfiguration.model.RouteConfig;
import com.solitardj9.apiService.applicationInterface.routeFilters.model.FilterForwardingMessage;
import com.solitardj9.apiService.applicationInterface.routeFilters.service.RouteFilterManager;

import reactor.core.publisher.Flux;

/**
 * A gateway route resolver which is used for dynamically refresh routes during the application runtime.
 *
 * @author          boto
 * Creation Date    25th June 2018
 */
@Component
public class RefreshableRoutesLocator implements RouteLocator {

    private final RouteLocatorBuilder builder;
    private final RoutesRefresher routesRefresher;

    private RouteLocatorBuilder.Builder routesBuilder;
    private Flux<Route> route = Flux.empty();

    @Autowired
    public RefreshableRoutesLocator(@NonNull final RouteLocatorBuilder builder, @NonNull final RoutesRefresher routesRefresher) {
        this.builder = builder;
        this.routesRefresher = routesRefresher;

        clearRoutes();
    }

    /**
     * Remove all routes.
     */
    public void clearRoutes() {
        routesBuilder = builder.routes();
    }

    /**
     * Add a new route. After adding all routes call 'buildRoutes'.
     */
    @NonNull
    public RefreshableRoutesLocator addRoute(List<RouteConfig> configs, RouteFilterManager routeFilterManager) throws URISyntaxException {

        for (RouteConfig config : configs) {
            //
            routesBuilder = routesBuilder.route(
                    config.getServiceName(),
                    r -> r.path(config.getPath())
                            //.filters(f -> f.filter(routeFilterManager.apply(new FilterForwardingMessage("Test Message"))))
                            .filters(f -> f.filter(routeFilterManager.apply(new FilterForwardingMessage("Test Message"))).retry(cfg -> cfg.setRetries(2).setStatuses(HttpStatus.INTERNAL_SERVER_ERROR)))
                            .uri(config.getRoutingUri())
            );

            routesBuilder = routesBuilder.route(
                    config.getServiceName(),
                    r -> r.path(config.getOpenApiPath())
                            .filters(f -> f.rewritePath(config.getOpenApiPath(), config.getSwaggerPath()))
                            .uri(config.getRoutingUri())
            );
        }

        return this;
    }

    /**
     * Call this method in order to publish all routes defined by 'addRoute' calls.
     */
    public void buildRoutes() {
        this.route = routesBuilder.build().getRoutes();
        routesRefresher.refreshRoutes();
    }

    @Override
    public Flux<Route> getRoutes() {
        return route;
    }
}