package ServiceWorker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.service.ServiceWorkerConfigManager;
import ServiceWorker.service.seviceWorker.ServiceWorkerSingle;

public class ServiceWorkerMain {
	
	public static void main(String[] args) {
		//
		System.out.println("Service Worker Main");
		
		ObjectMapper om = new ObjectMapper();
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("key1", "key1_value");
		httpHeaders.add("key2", "key2_value");
		
		try {
			String jsonStringHeaders = om.writeValueAsString(httpHeaders);
			System.out.println(jsonStringHeaders);
			
			HttpHeaders headers2 = om.readValue(jsonStringHeaders, HttpHeaders.class);
			System.out.println(headers2.toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		String config_single = "{\r\n" + 
				"	\"name\" : \"API-ACCU-WEATHER-V1-FORECAST_DAILY\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"https\",\r\n" + 
				"		\"host\" : \"api.accuweather.com\",\r\n" + 
				"		\"method\" : \"GET\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"forecasts/v1/daily/FUNCTION( , headers, x-api-weather-forecast-days)day/FUNCTION( , headers, x-api-weather-location-key)\",\r\n" + 
				"					\"headers\" : null,\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"apiKey\" : \"\",\r\n" + 
				"      					\"languag\" : \"FUNCTION( , headers, x-api-languag)\",\r\n" + 
				"      					\"details\" : true,\r\n" + 
				"      					\"metric\" : true\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : null\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(responseStatus, code, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(responseStatus, code, 200)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION( , responseStatus, code)\",\r\n" + 
				"					\"message\" : \"\",\r\n" + 
				"					\"ret\" : \"FUNCTION( , responseBody, $)\"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		String config_composite = "{\r\n" + 
				"  \"name\" : \"API-WEATHER-FORECAST_DAILY_HOURLY\",\r\n" + 
				"  \"type\" : \"composite\",\r\n" + 
				"  \"sequence\" : [\r\n" + 
				"    \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"    \"API-WEATHER-FORECAST_HOURLY\"\r\n" + 
				"  ]\r\n" + 
				"}";
				
		for (int i = 0 ; i < 1 ; i++) {
			ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
			
			serviceWorkerConfigManager.addConfig(config_single);
			//System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY"));
			
			serviceWorkerConfigManager.addConfig(config_composite);
			//System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY_HOURLY"));
			
			//System.out.println(serviceWorkerConfigManager.getAllConfigs());
			
			ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(serviceWorkerConfigManager.getConfigAsString("API-ACCU-WEATHER-V1-FORECAST_DAILY"));
			
			HttpHeaders inputHttpHeaders = new HttpHeaders();
			//inputHttpHeaders.add("key_INPUT_1", "Aaa");
			//inputHttpHeaders.add("key_INPUT_2", "Bbb");
			inputHttpHeaders.add("x-api-languag", "kr-ko");
			inputHttpHeaders.add("x-api-weather-forecast-days", "1");
			inputHttpHeaders.add("x-api-weather-location-key", "226081");
			serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
			
			Map<String, Object> inputQueryParams = new HashMap<>();
			//inputQueryParams.put("key_INPUT_1", "deviceId test");
			//inputQueryParams.put("key_INPUT_1", "deviceId test2");
			//inputQueryParams.put("key_INPUT_1", "deviceId test3");
			//inputQueryParams.put("key_INPUT_1", "deviceId test3");
			//inputQueryParams.put("key_INPUT_2", 35.5);
			serviceWorkerSingle.setInputQueryParams(inputQueryParams);
			
			//MyListRequest myListRequest = new MyListRequest("bk.cha", 21);
			//try {
			//	serviceWorkerSingle.setInputRequestBody(om.writeValueAsString(myListRequest));
			//} catch (JsonProcessingException e) {
			//	e.printStackTrace();
			//}
			serviceWorkerSingle.setInputRequestBody(null);
			
			System.out.println("//---------------------------------------------");
			System.out.println("InputHttpHeaders = " + serviceWorkerSingle.getInputHttpHeaders());
			System.out.println("InputQueryParams = " + serviceWorkerSingle.getInputQueryParams());
			System.out.println("InputRequestBody = " + serviceWorkerSingle.getInputRequestBody());
			System.out.println("//---------------------------------------------");
			
			long startTime = System.nanoTime();
			serviceWorkerSingle.doWork();
			long endTime = System.nanoTime();
			long diffTime = endTime - startTime;
			System.out.println("diffTime = " + diffTime);
			long diffTimeInMilli = diffTime / 1000000L;
			System.out.println("diffTimeInMilli = " + diffTimeInMilli);
			
			System.out.println("//---------------------------------------------");
			System.out.println("OutputPath = " + serviceWorkerSingle.getOutgoingPath());
			System.out.println("OutputHttpHeaders = " + serviceWorkerSingle.getOutgoingHttpHeaders());
			System.out.println("OutputQueryParams = " + serviceWorkerSingle.getOutgoingQueryParams());
			System.out.println("OutgoingRequestBody = " + serviceWorkerSingle.getOutgoingRequestBody());
			System.out.println("InputResponseBody = " + serviceWorkerSingle.getInputResponseBody());
			System.out.println("OutgoingResponseBody = " + serviceWorkerSingle.getOutgoingResponseBody());
			System.out.println("//---------------------------------------------");
		}
	}
}