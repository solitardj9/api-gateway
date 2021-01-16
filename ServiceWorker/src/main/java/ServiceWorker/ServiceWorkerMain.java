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
				"	\"name\" : \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"http\",\r\n" + 
				"		\"host\" : \"127.0.0.1:19370\" ,\r\n" + 
				"		\"method\" : \"PUT\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(headers, key_INPUT_1, [Aaa])\", \"TRIGGER(queryParams, key_INPUT_1, deviceId test)\", \"TRIGGER(requestBody, $.id, bk.cha)\"],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"service/service-a/b2b/test/api/getList\",\r\n" + 
				"					\"headers\" : {\r\n" + 
				"						\"key_FD_1\" : [\"FUNCTION( , headers, key_INPUT_1)\"],\r\n" +
//				"						\"key_FD_1\" : [\"FUNCTION_URL_ENCODE(FUNCTION( , headers, key_INPUT_1), UTF_8)\"],\r\n" +
				"      					\"key_FD_2\" : [\"FUNCTION( , headers, key_INPUT_2)\"],\r\n" + 
				"      					\"key_FD_3\" : [\"3\", \"4\"],\r\n" + 
				"      					\"x-location-info\" : [\"FUNCTION( , headers, x-location-info)\"]\r\n" + 
				"    				},\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"key_FD_1\" : \"FUNCTION( , queryParams, key_INPUT_1)\",\r\n" + 
				"      					\"key_FD_2\" : \"FUNCTION( , queryParams, key_INPUT_2)\",\r\n" + 
				"      					\"key_FD_3\" : 2\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : {\r\n" + 
				"	  					\"id\" : \"FUNCTION( , requestBody, $.id)\",\r\n" + 
				"      					\"age\" : \"FUNCTION( , requestBody, $.age)\"\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(headers, key_INPUT_2, [Bbb])\", \"TRIGGER(queryParams, key_INPUT_1, deviceId test2)\"],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"service-a/test/getList\",\r\n" + 
				"					\"headers\" : {\r\n" + 
				"						\"key_FD_1\" : [\"FUNCTION( , headers, key_INPUT_1)\"],\r\n" + 
				"      					\"key_FD_2\" : [\"FUNCTION( , headers, key_INPUT_2)\"]\r\n" + 
				"    				},\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"key_FD_1\" : \"FUNCTION( , queryParams, key_INPUT_1)\",\r\n" + 
				"      					\"key_FD_2\" : \"FUNCTION( , queryParams, key_INPUT_2)\"\r\n" + 
				"    				},\r\n" + 
				"    				\"requestBody\" : {\r\n" + 
				"	  					\"id\" : \"FUNCTION( , requestBody, $.id)\",\r\n" + 
				"      					\"age\" : \"FUNCTION( , requestBody, $.age)\"\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(headers, key_INPUT_2, [Ccc])\", \"TRIGGER(queryParams, key_INPUT_1, deviceId test3)\"],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"service-a/test/getList\",\r\n" + 
				"					\"headers\" : {\r\n" + 
				"    				},\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"    				},\r\n" + 
				"    				\"requestBody\" : {\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(responseStatus, CODE, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"TRIGGER(responseStatus, code, 200)\", \"TRIGGER(responseBody, $.message, success)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION( , responseBody, $.code)\",\r\n" + 
				"					\"message\" : \"FUNCTION( , responseBody, $.message)\",\r\n" + 
				"					\"ret\" : {\r\n" + 
				"						\"retKey1\" : \"FUNCTION( , responseBody, $.ret.retKey1)\",\r\n" + 
				"						\"retKey2\" : \"FUNCTION( , responseBody, $.ret.retKey2)\"\r\n" + 
				"					}\r\n" + 
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
			
			ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY"));
//			ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(serviceWorkerConfigManager.getConfigAsString("API-ACCU-WEATHER-V1-FORECAST_DAILY"));
			
			
			String tmpHeaderInfo = "{\r\n" + 
					"    \"locationType\" : \"geoPosition\",\r\n" + 
					"    \"info\" : {\r\n" + 
					"        \"latitude\" : 37.511734,\r\n" + 
					"        \"longitude\" : 127.037517\r\n" + 
					"    }\r\n" + 
					"}";
			
			HttpHeaders inputHttpHeaders = new HttpHeaders();
			inputHttpHeaders.add("key_INPUT_1", "Aaa");
			inputHttpHeaders.add("key_INPUT_2", "Bbb");
			inputHttpHeaders.add("x-location-info", tmpHeaderInfo);
//			inputHttpHeaders.add("x-api-languag", "kr-ko");
//			inputHttpHeaders.add("x-api-weather-forecast-days", "1");
//			inputHttpHeaders.add("x-api-weather-location-key", "226081");
			serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
			
			Map<String, Object> inputQueryParams = new HashMap<>();
			inputQueryParams.put("key_INPUT_1", "deviceId test");
			//inputQueryParams.put("key_INPUT_1", "deviceId test2");
			//inputQueryParams.put("key_INPUT_1", "deviceId test3");
			//inputQueryParams.put("key_INPUT_1", "deviceId test3");
			//inputQueryParams.put("key_INPUT_2", 35.5);
			serviceWorkerSingle.setInputQueryParams(inputQueryParams);
			
			MyListRequest myListRequest = new MyListRequest("bk.cha", 21);
			try {
				serviceWorkerSingle.setInputRequestBody(om.writeValueAsString(myListRequest));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
//			serviceWorkerSingle.setInputRequestBody(null);
			
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