package serviceWorker2.model.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.web.util.UriComponentsBuilder;

public class UrlHostUtil {
	//
	public static UriComponentsBuilder makeUriComponentsBuilder(String shceme, String url, String path) {
		//
		String host = getHost(url); 
		String port = getPort(url);
		
		if (!port.equals("-1")) {
			if (checkUrlValidation(host, port)) {
				UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
																				.scheme(shceme)
																				.host(host)
																				.port(port)
																				.path(path);
				
				return uriComponentsBuilder;
			}
		}
		else {
			if (checkHostValidation(host)) {
				UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
																				.scheme(shceme)
																				.host(host)
																				.path(path);
				
				return uriComponentsBuilder;
			}
		}
		
		return null;
	}
	
	private static Boolean checkUrlValidation(String host, String port) {
		//
		if (checkHostValidation(host) == true & checkPortValidation(port) == true) {
			return true;
		}
		return false;
	}
	
	private static Boolean checkHostValidation(String host) {
		//
		if (host != null) {
			return true;
		}
		return false;
	}
	
	private static Boolean checkPortValidation(String port) {
		//
		if (port != null && !port.equals("-1")) {
			return true;
		}
		return false;
	}
	
	private static String getHost(String url) {
		//
		URL tmpUrl = null;
		try {
			tmpUrl = new URL(url);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		String host = tmpUrl.getHost();
		
		return host;
	}
	
	private static String getPort(String url) {
		//
		URL tmpUrl = null;
		try {
			tmpUrl = new URL(url);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();			
			return null;
		}
		
		Integer port = tmpUrl.getPort();
		
		// if the port is not explicitly specified in the input, it will be -1.
		return port.toString();
	}
}