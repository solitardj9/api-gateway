package com.solitardj9.apiService.applicationInterface.routeFilters.service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalPostFilterManager implements GlobalFilter, Ordered {

    final Logger logger = LogManager.getLogger(GlobalPostFilterManager.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //
        logger.info("[GlobalPreFilterManager].filter : Global Post Filter");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}