package com.solitardj9.apiService.applicationInterface.test.broadcaster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.solitardj9.apiService.systemInterface.httpInterface.service.HttpProxyAdaptor;



@Service("broadcaster")
public class BroadcasterImpl implements Broadcaster {

	final Logger logger = LogManager.getLogger(BroadcasterImpl.class);
	
	@Autowired
	DiscoveryClient discoveryClient;
	
	@Autowired
	HttpProxyAdaptor httpProxyAdaptor;
	
	@Value("${server.port}")
	private Integer port;
	
	@Override
	public void doTest() {
		//
//		List<ServiceInstance> instances = new ArrayList<>();
//		
//		List<String> serviceNames = discoveryClient.getServices();
//		
//		for (String iter : serviceNames) {
//			List<ServiceInstance> tmpInstances = discoveryClient.getInstances(iter);
//			instances.addAll(tmpInstances);
//		}
		
		String myHostname = null;
		try {
			myHostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		logger.info("myHostname = " + myHostname);
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<ServiceInstance> instances = discoveryClient.getInstances("gateway-service");
		
		for (ServiceInstance iter : instances) {
			
			logger.info("Instance id = " + iter.getInstanceId());
			logger.info("Service id = " + iter.getServiceId());
			logger.info("Instance hostname = " + iter.getHost());
			logger.info("Instance scheme = " + iter.getScheme());
			logger.info("Instance uri = " + iter.getUri());
			logger.info("Instance uri host = " + iter.getUri().getHost());
			logger.info("Instance uri path = " + iter.getUri().getPath());
			logger.info("Instance uri port = " + iter.getUri().getPort());
			logger.info("Instance metadata = " + iter.getMetadata());
			
			if ((!iter.getHost().equals(myHostname)) || (iter.getPort() != port)) {
				logger.info("httpProxy call starts");
				httpProxyAdaptor.executeHttpProxy(iter.getScheme(), 
												  iter.getUri().getHost() + ":" + iter.getUri().getPort(), 
												  "management/broadcast", 
												  null, 
												  HttpMethod.PUT, 
												  null, 
												  null);
				logger.info("httpProxy call ends");
			}
			
			logger.info("_");
		}
	}
}