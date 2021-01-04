package ServiceWorker;

import ServiceWorker.model.seviceWorker.JsonUtil;
import ServiceWorker.service.ServiceWorkerConfigManager;

public class ServiceWorkerMain {
	
	public static void main(String[] args) {
		//
		System.out.println("Service Worker Main");
		
//		ObjectMapper om = new ObjectMapper();
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("key1", "key1_value");
//		headers.add("key2", "key2_value");
//		
//		try {
//			String jsonStringHeaders = om.writeValueAsString(headers);
//			System.out.println(jsonStringHeaders);
//			
//			HttpHeaders headers2 = om.readValue(jsonStringHeaders, HttpHeaders.class);
//			System.out.println(headers2.toString());
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
		
		String config_single = "{\r\n" + 
				"  \"name\" : \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"  \"type\" : \"single\",\r\n" + 
				"  \"config\" : {\r\n" + 
				"    \"headers\" : {\r\n" + 
				"      \"key_FD_1\" : \"[FUNCTION( , headers, key_INPUT_1)]\",\r\n" + 
				"      \"key_FD_2\" : \"[FUNCTION( , headers, key_INPUT_2)]\"\r\n" + 
				"    },\r\n" + 
				"    \"queryParams\" : {\r\n" + 
				"      \"key_FD_1\" : \"[FUNCTION( , queryParams, key_INPUT_1)]\",\r\n" + 
				"      \"key_FD_2\" : \"[FUNCTION( , queryParams, key_INPUT_2)]\"\r\n" + 
				"    },\r\n" + 
				"    \"body\" : {\r\n" + 
				"      \"code\" : \"[FUNCTION( , body, $.code)]\",\r\n" + 
				"      \"message\" : \"[FUNCTION( , body, $.message)]\",\r\n" + 
				"      \"ret\" : {\r\n" + 
				"        \"retKey1\" : \"[FUNCTION( , body, $.ret.retKey1)]\",\r\n" + 
				"        \"retKey2\" : \"[FUNCTION( , body, $.ret.retKey2)]\"\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
		String config_composite = "{\r\n" + 
				"  \"name\" : \"API-WEATHER-FORECAST_DAILY_HOURLY\",\r\n" + 
				"  \"type\" : \"composite\",\r\n" + 
				"  \"sequence\" : [\r\n" + 
				"    \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"    \"API-WEATHER-FORECAST_HOURLY\"\r\n" + 
				"  ]\r\n" + 
				"}";
				
		
		ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		
		serviceWorkerConfigManager.addConfig(config_single);
		System.out.println(serviceWorkerConfigManager.getConfig("API-WEATHER-FORECAST_DAILY"));
		
		serviceWorkerConfigManager.addConfig(config_composite);
		System.out.println(serviceWorkerConfigManager.getConfig("API-WEATHER-FORECAST_DAILY_HOURLY"));
		
		System.out.println(serviceWorkerConfigManager.getAllConfigs());
		
		Object body = JsonUtil.readValue(config_single, "$.config.body");
		System.out.println(body);
		Object code = JsonUtil.readValue(body, "$.code");
		System.out.println(code);
		
	}
}