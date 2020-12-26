package com.solitardj9.apiService.serviceInterface.admin.management.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.apiService.applicationInterface.routeConfiguration.service.RouteManager;

@RestController
@RequestMapping(value="/management")
public class ManagementController {

    private static final Logger logger = LogManager.getLogger(ManagementController.class);

    @Autowired
    RouteManager routeManager;

    @SuppressWarnings("rawtypes")
    @PutMapping(value="/service/{service}/stop")
    public ResponseEntity stopService(@PathVariable("service") String service,
                                      @RequestBody(required=false) String requestBody) {
        //
        logger.info("[ManagementController].stopService is called : service = " + service);

        routeManager.stopRoute(service);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping(value="/service/{service}/start")
    public ResponseEntity startService(@PathVariable("service") String service,
                                       @RequestBody(required=false) String requestBody) {
        //
        logger.info("[ManagementController].startService is called : service = " + service);

        routeManager.startRoute(service);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @SuppressWarnings("rawtypes")
    @GetMapping(value="/routes")
    public ResponseEntity getRoutes() {
        //
        logger.info("[ManagementController].getRoutes is called");

        return new ResponseEntity<>(routeManager.getRoutes(), HttpStatus.OK);
    }
}