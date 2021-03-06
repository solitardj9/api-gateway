package com.solitardj9.apiService.applicationInterface.routeConfiguration.service.impl;

/*
 * Copyright (c) 2018 - 2020 by Botorabi. All rights reserved.
 * https://github.com/botorabi/HomieCenter
 *
 * License: MIT License (MIT), read the LICENSE text in
 *          main directory for more details.
 */
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.*;
import org.springframework.stereotype.Component;


/**
 * A gateway component responsible for publishing route changes.
 *
 * @author          boto
 * Creation Date    25th June 2018
 */
@Component
public class RoutesRefresher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    public void refreshRoutes() {
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}