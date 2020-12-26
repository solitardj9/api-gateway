package com.solitardj9.apiService.applicationInterface.routeFilters.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.solitardj9.apiService.applicationInterface.routeFilters.model.FilterForwardingMessage;

@Component
public class RouteFilterManager extends AbstractGatewayFilterFactory<FilterForwardingMessage> implements Ordered {

    final Logger logger = LogManager.getLogger(RouteFilterManager.class);

    public RouteFilterManager() {
        super(FilterForwardingMessage.class);
    }

    @Override
    public GatewayFilter apply(FilterForwardingMessage filterForwardingMessage) {
        //
        return (exchange, chain) -> {

            logger.info("exchange.getRequest().getPath() = " + exchange.getRequest().getPath().toString());

            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 0;
    }
}