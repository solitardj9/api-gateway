package serviceWorker2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.MyListRequest;
import serviceWorker2.model.config.ServiceWorkerConfigBase;
import serviceWorker2.model.config.ServiceWorkerConfigComposite;
import serviceWorker2.model.config.ServiceWorkerConfigSingle;
import serviceWorker2.model.worker.ServiceWorkerComposite;
import serviceWorker2.model.worker.ServiceWorkerSingle;
import serviceWorker2.service.ServiceWorkerConfigManager;


public class ServiceWorker2Main {
	
	private static ObjectMapper om = new ObjectMapper();
	
	public static void main(String[] args) {
		//
		System.out.println("Service Worker 2 Main");
		
		//doSingleWorkerTest();
		
		//doSingleWorkerTest2();
		
		//doSingleWorkerTest3();
		
		doCompoisteWorkerTest();
		
		
//		String outgoingResponseBody = "[{\"Version\":1,\"Key\":\"226081\",\"Type\":\"City\",\"Rank\":10,\"LocalizedName\":\"Seoul\",\"EnglishName\":\"Seoul\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"KR\",\"LocalizedName\":\"South Korea\",\"EnglishName\":\"South Korea\"},\"AdministrativeArea\":{\"ID\":\"11\",\"LocalizedName\":\"Seoul\",\"EnglishName\":\"Seoul\",\"Level\":1,\"LocalizedType\":\"Special City\",\"EnglishType\":\"Special City\",\"CountryID\":\"KR\"},\"TimeZone\":{\"Code\":\"KST\",\"Name\":\"Asia/Seoul\",\"GmtOffset\":9.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":37.537,\"Longitude\":126.97,\"Elevation\":{\"Metric\":{\"Value\":19.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":62.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQuality\",\"AirQuality-Regional\",\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"Alerts\",\"DailyLocalIndices\",\"ForecastConfidence\",\"FutureRadar\",\"HourlyLocalIndices\",\"MinuteCast\",\"PremiumAirQuality\",\"Radar\"]}]";
//		System.out.println(JSONPath.read(outgoingResponseBody, "[0].Key").toString());
//		
//		String outgoingResponseBody2 = "{\"code\":200,\"message\":\"sucess\",\"ret\":[{\"Version\":1,\"Key\":\"226081\",\"Type\":\"City\",\"Rank\":10,\"LocalizedName\":\"Seoul\",\"EnglishName\":\"Seoul\",\"PrimaryPostalCode\":\"\",\"Region\":{\"ID\":\"ASI\",\"LocalizedName\":\"Asia\",\"EnglishName\":\"Asia\"},\"Country\":{\"ID\":\"KR\",\"LocalizedName\":\"South Korea\",\"EnglishName\":\"South Korea\"},\"AdministrativeArea\":{\"ID\":\"11\",\"LocalizedName\":\"Seoul\",\"EnglishName\":\"Seoul\",\"Level\":1,\"LocalizedType\":\"Special City\",\"EnglishType\":\"Special City\",\"CountryID\":\"KR\"},\"TimeZone\":{\"Code\":\"KST\",\"Name\":\"Asia/Seoul\",\"GmtOffset\":9.0,\"IsDaylightSaving\":false,\"NextOffsetChange\":null},\"GeoPosition\":{\"Latitude\":37.537,\"Longitude\":126.97,\"Elevation\":{\"Metric\":{\"Value\":19.0,\"Unit\":\"m\",\"UnitType\":5},\"Imperial\":{\"Value\":62.0,\"Unit\":\"ft\",\"UnitType\":0}}},\"IsAlias\":false,\"SupplementalAdminAreas\":[],\"DataSets\":[\"AirQuality\",\"AirQuality-Regional\",\"AirQualityCurrentConditions\",\"AirQualityForecasts\",\"Alerts\",\"DailyLocalIndices\",\"ForecastConfidence\",\"FutureRadar\",\"HourlyLocalIndices\",\"MinuteCast\",\"PremiumAirQuality\",\"Radar\"]}]}";
//		System.out.println(JSONPath.read(outgoingResponseBody2, "$.ret[0].Key").toString());
		
	}
	
	private static void doSingleWorkerTest() {
		//
		String config_single = "{\r\n" + 
				"	\"name\" : \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"http\",\r\n" + 
				"		\"host\" : \"127.0.0.1:19370\" ,\r\n" + 
				"		\"method\" : \"PUT\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_URLENCODE(FUNCTION_TRIGGER(headers, key_INPUT_1, [Aaa]), UTF-8)\", \"FUNCTION_TRIGGER(queryParams, key_INPUT_1, deviceId test)\", \"FUNCTION_TRIGGER(requestBody, $.id, bk.cha)\"],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"FUNCTION_STRINGFORMAT(service/service-a/b2b/{1}/api/{2}, test, getList)\",\r\n" + 
				"					\"headers\" : {\r\n" + 
				"						\"key_FD_1\" : [\"FUNCTION_GET( , headers, key_INPUT_1)\"],\r\n" + 
				"      					\"key_FD_2\" : [\"FUNCTION_GET( , headers, key_INPUT_2)\"],\r\n" + 
				"      					\"key_FD_3\" : [\"3\", \"4\"],\r\n" + 
				"      					\"x-location-info\" : [\"FUNCTION_GET( , headers, x-location-info)\"]\r\n" + 
				"    				},\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"key_FD_1\" : \"FUNCTION_GET( , queryParams, key_INPUT_1)\",\r\n" + 
				"      					\"key_FD_2\" : \"FUNCTION_GET( , queryParams, key_INPUT_2)\",\r\n" + 
				"      					\"key_FD_3\" : 2\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : {\r\n" + 
				"	  					\"id\" : \"FUNCTION_GET( , requestBody, $.id)\",\r\n" + 
//				"      					\"age\" : \"FUNCTION_GET( , requestBody, $.age)\"\r\n" + 
				"      					\"age\" : 23\r\n" +
				"					}\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(headers, key_INPUT_2, [Bbb])\", \"FUNCTION_TRIGGER(queryParams, key_INPUT_1, deviceId test2)\"],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"service-a/test/getList\",\r\n" + 
				"					\"headers\" : {\r\n" + 
				"						\"key_FD_1\" : [\"FUNCTION_GET( , headers, key_INPUT_1)\"],\r\n" + 
				"      					\"key_FD_2\" : [\"FUNCTION_GET( , headers, key_INPUT_2)\"]\r\n" + 
				"    				},\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"key_FD_1\" : \"FUNCTION_GET( , queryParams, key_INPUT_1)\",\r\n" + 
				"      					\"key_FD_2\" : \"FUNCTION_GET( , queryParams, key_INPUT_2)\"\r\n" + 
				"    				},\r\n" + 
				"    				\"requestBody\" : {\r\n" + 
				"	  					\"id\" : \"FUNCTION( , requestBody, $.id)\",\r\n" + 
				"      					\"age\" : \"FUNCTION( , requestBody, $.age)\"\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(headers, key_INPUT_2, [Ccc])\", \"FUNCTION_TRIGGER(queryParams, key_INPUT_1, deviceId test3)\"],\r\n" + 
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
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, CODE, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, CODE, 200)\", \"FUNCTION_TRIGGER(responseBody, $.message, success)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION_GET( , responseBody, $.code)\",\r\n" + 
				"					\"message\" : \"FUNCTION_GET( , responseBody, $.message)\",\r\n" + 
				"					\"ret\" : {\r\n" + 
				"						\"retKey1\" : \"FUNCTION_GET( , responseBody, $.ret.retKey1)\",\r\n" + 
				"						\"retKey2\" : \"FUNCTION_GET( , responseBody, $.ret.retKey2)\"\r\n" + 
				"					}\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
		for (int i = 0 ; i < 1 ; i++) {
			ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
			
			serviceWorkerConfigManager.addConfig(config_single);
			
			ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle((ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig("API-WEATHER-FORECAST_DAILY"));
			
			HttpHeaders inputHttpHeaders = new HttpHeaders();
			inputHttpHeaders.add("key_INPUT_1", "Aaa");
			inputHttpHeaders.add("key_INPUT_1", "AAA");
			inputHttpHeaders.add("key_INPUT_2", "Bbb");
			inputHttpHeaders.add("key_INPUT_2", "BBB");
			serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
			
			Map<String, Object> inputQueryParams = new HashMap<>();
			inputQueryParams.put("key_INPUT_1", "deviceId test");
			inputQueryParams.put("key_INPUT_2", 35.5);
			serviceWorkerSingle.setInputQueryParams(inputQueryParams);
			
			MyListRequest myListRequest = new MyListRequest("bk.cha", 21);
			try {
				serviceWorkerSingle.setInputRequestBody(om.writeValueAsString(myListRequest));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			System.out.println("//---------------------------------------------");
			System.out.println("InputHttpHeaders = " + serviceWorkerSingle.getInputHttpHeaders());
			System.out.println("InputQueryParams = " + serviceWorkerSingle.getInputQueryParams());
			System.out.println("InputRequestBody = " + serviceWorkerSingle.getInputRequestBody());
			System.out.println("DcInputRequestBody = " + serviceWorkerSingle.getDcInputRequestBody().jsonString());
			System.out.println("//---------------------------------------------");
			
			long startTime = System.nanoTime();
			serviceWorkerSingle.executeWork();
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
	
	private static void doSingleWorkerTest2() {
		//
		String config_single = "{\r\n" + 
				"	\"name\" : \"API-ACCU-WEATHER-V1-LOCATION\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"https\",\r\n" + 
				"		\"host\" : \"api.accuweather.com\" ,\r\n" + 
				"		\"method\" : \"GET\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"locations/v1/cities/translate.json\",\r\n" + 
				"					\"headers\" : null,\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"apiKey\" : \"183ce836db564ac6a9c2ceab9fb41c5e\",\r\n" + 
				"      					\"languag\" : \"FUNCTION_GET( , headers, x-api-languag, 0)\",\r\n" + 
				"      					\"q\" : \"FUNCTION_GET( , headers, x-api-city, 0)\"\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : null\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 200)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION_GET( , responseStatus, code)\",\r\n" + 
				"					\"message\" : \"sucess\",\r\n" + 
				"					\"ret\" : \"FUNCTION_GET( , responseBody, $)\"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
		ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		
		serviceWorkerConfigManager.addConfig(config_single);
		
		ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle((ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig("API-ACCU-WEATHER-V1-LOCATION"));
		
		HttpHeaders inputHttpHeaders = new HttpHeaders();
		inputHttpHeaders.add("x-api-languag", "kr");
		inputHttpHeaders.add("x-api-country", "ko");
		inputHttpHeaders.add("x-api-city", "seoul");
		serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
		
		Map<String, Object> inputQueryParams = new HashMap<>();
		serviceWorkerSingle.setInputQueryParams(inputQueryParams);
		
		serviceWorkerSingle.setInputRequestBody(null);
		
		System.out.println("//---------------------------------------------");
		System.out.println("InputHttpHeaders = " + serviceWorkerSingle.getInputHttpHeaders());
		System.out.println("InputQueryParams = " + serviceWorkerSingle.getInputQueryParams());
		System.out.println("InputRequestBody = " + serviceWorkerSingle.getInputRequestBody());
		System.out.println("DcInputRequestBody = " + serviceWorkerSingle.getDcInputRequestBody().jsonString());
		System.out.println("//---------------------------------------------");
		
		serviceWorkerSingle.executeWork();
		
		System.out.println("//---------------------------------------------");
		System.out.println("OutputPath = " + serviceWorkerSingle.getOutgoingPath());
		System.out.println("OutputHttpHeaders = " + serviceWorkerSingle.getOutgoingHttpHeaders());
		System.out.println("OutputQueryParams = " + serviceWorkerSingle.getOutgoingQueryParams());
		System.out.println("OutgoingRequestBody = " + serviceWorkerSingle.getOutgoingRequestBody());
		System.out.println("InputResponseBody = " + serviceWorkerSingle.getInputResponseBody());
		System.out.println("OutgoingResponseBody = " + serviceWorkerSingle.getOutgoingResponseBody());
		System.out.println("//---------------------------------------------");
	}
	
	private static void doSingleWorkerTest3() {
		//
		String config_single = "{\r\n" + 
				"	\"name\" : \"API-ACCU-WEATHER-V1-FORECAST_DAILY\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"https\",\r\n" + 
				"		\"host\" : \"api.accuweather.com\" ,\r\n" + 
				"		\"method\" : \"GET\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"FUNCTION_STRINGFORMAT(forecasts/v1/daily/{1}day/{2}, FUNCTION_GET( , headers, x-api-day, 0), FUNCTION_GET( , headers, x-api-city, 0))\",\r\n" + 
				"					\"headers\" : null,\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"apiKey\" : \"183ce836db564ac6a9c2ceab9fb41c5e\",\r\n" + 
				"      					\"languag\" : \"FUNCTION_STRINGFORMAT({1}-{2}, FUNCTION_GET( , headers, x-api-languag, 0), FUNCTION_GET( , headers, x-api-country, 0))\",\r\n" + 
				"      					\"details\" : true,\r\n" + 
				"      					\"metric\" : true\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : null\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 200)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION_GET( , responseStatus, code)\",\r\n" + 
				"					\"message\" : \"sucess\",\r\n" + 
				"					\"ret\" : \"FUNCTION_GET( , responseBody, $)\"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
		ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		
		serviceWorkerConfigManager.addConfig(config_single);
		
		ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle((ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig("API-ACCU-WEATHER-V1-FORECAST_DAILY"));
		
		HttpHeaders inputHttpHeaders = new HttpHeaders();
		inputHttpHeaders.add("x-api-languag", "kr");
		inputHttpHeaders.add("x-api-country", "ko");
		//inputHttpHeaders.add("x-api-city", "Seoul");
		inputHttpHeaders.add("x-api-city", "226081");
		inputHttpHeaders.add("x-api-day", "1");
		serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
		
		Map<String, Object> inputQueryParams = new HashMap<>();
		serviceWorkerSingle.setInputQueryParams(inputQueryParams);
		
		serviceWorkerSingle.setInputRequestBody(null);
		
		System.out.println("//---------------------------------------------");
		System.out.println("InputHttpHeaders = " + serviceWorkerSingle.getInputHttpHeaders());
		System.out.println("InputQueryParams = " + serviceWorkerSingle.getInputQueryParams());
		System.out.println("InputRequestBody = " + serviceWorkerSingle.getInputRequestBody());
		System.out.println("DcInputRequestBody = " + serviceWorkerSingle.getDcInputRequestBody().jsonString());
		System.out.println("//---------------------------------------------");
		
		serviceWorkerSingle.executeWork();
		
		System.out.println("//---------------------------------------------");
		System.out.println("OutputPath = " + serviceWorkerSingle.getOutgoingPath());
		System.out.println("OutputHttpHeaders = " + serviceWorkerSingle.getOutgoingHttpHeaders());
		System.out.println("OutputQueryParams = " + serviceWorkerSingle.getOutgoingQueryParams());
		System.out.println("OutgoingRequestBody = " + serviceWorkerSingle.getOutgoingRequestBody());
		System.out.println("InputResponseBody = " + serviceWorkerSingle.getInputResponseBody());
		System.out.println("OutgoingResponseBody = " + serviceWorkerSingle.getOutgoingResponseBody());
		System.out.println("//---------------------------------------------");
	}
	
	private static void doCompoisteWorkerTest() {
		
		String config_single_location = "{\r\n" + 
				"	\"name\" : \"API-ACCU-WEATHER-V1-LOCATION\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"https\",\r\n" + 
				"		\"host\" : \"api.accuweather.com\" ,\r\n" + 
				"		\"method\" : \"GET\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"locations/v1/cities/translate.json\",\r\n" + 
				"					\"headers\" : null,\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"apiKey\" : \"183ce836db564ac6a9c2ceab9fb41c5e\",\r\n" + 
				"      					\"language\" : \"FUNCTION_STRINGFORMAT({1}-{2}, FUNCTION_GET( , headers, x-api-country, 0), FUNCTION_GET( , headers, x-api-language, 0))\",\r\n" + 
				"      					\"q\" : \"FUNCTION_GET( , headers, x-api-city, 0)\"\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : null\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 200)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION_GET( , responseStatus, code)\",\r\n" + 
				"					\"message\" : \"sucess\",\r\n" + 
				"					\"ret\" : \"FUNCTION_GET( , responseBody, $)\"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
		String config_single_forecast = "{\r\n" + 
				"	\"name\" : \"API-ACCU-WEATHER-V1-FORECAST_DAILY\",\r\n" + 
				"	\"type\" : \"single\",\r\n" + 
				"	\"config\" : {\r\n" + 
				"		\"scheme\" : \"https\",\r\n" + 
				"		\"host\" : \"api.accuweather.com\" ,\r\n" + 
				"		\"method\" : \"GET\",\r\n" + 
				"		\"requestRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [],\r\n" + 
				"				\"action\" : {\r\n" + 
				"					\"path\" : \"FUNCTION_STRINGFORMAT(forecasts/v1/daily/{1}day/{2}, FUNCTION_GET(COMPOSITE, headers, x-api-day, 0), FUNCTION_GET(API-ACCU-WEATHER-V1-LOCATION, responseBody, $.ret[0].Key))\",\r\n" + 
				"					\"headers\" : null,\r\n" + 
				"    				\"queryParams\" : {\r\n" + 
				"      					\"apiKey\" : \"183ce836db564ac6a9c2ceab9fb41c5e\",\r\n" + 
				"      					\"language\" : \"FUNCTION_GET(API-ACCU-WEATHER-V1-LOCATION, queryParams, language)\",\r\n" + 
				"      					\"details\" : true,\r\n" + 
				"      					\"metric\" : true\r\n" + 
				"    				},\r\n" + 
				"					\"requestBody\" : null\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		],\r\n" + 
				"		\"responseRule\" : [\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 404)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : 404,\r\n" + 
				"					\"message\" : \"error_message_404\"\r\n" + 
				"				}\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"condition\" : [\"FUNCTION_TRIGGER(responseStatus, code, 200)\"],\r\n" + 
				"				\"responseBody\" : {\r\n" + 
				"					\"code\" : \"FUNCTION_GET( , responseStatus, code)\",\r\n" + 
				"					\"message\" : \"sucess\",\r\n" + 
				"					\"ret\" : \"FUNCTION_GET( , responseBody, $)\"\r\n" + 
				"				}\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}\r\n" + 
				"}";
		
		String config_single_forecast_composite = "{\r\n" + 
				"  \"name\" : \"API-ACCU-WEATHER-V1-FORECAST_DAILY_COMPOSITE\",\r\n" + 
				"  \"type\" : \"composite\",\r\n" + 
				"  \"sequence\" : [\r\n" + 
				"    \"API-ACCU-WEATHER-V1-LOCATION\",\r\n" + 
				"    \"API-ACCU-WEATHER-V1-FORECAST_DAILY\"\r\n" + 
				"  ]\r\n" + 
				"}";
		
		ServiceWorkerConfigManager serviceWorkerConfigManager = new ServiceWorkerConfigManager();
		
		serviceWorkerConfigManager.addConfig(config_single_forecast_composite);
		serviceWorkerConfigManager.addConfig(config_single_location);
		serviceWorkerConfigManager.addConfig(config_single_forecast);
		
		//----------------------------------------------------------------------------
		HttpHeaders inputHttpHeaders = new HttpHeaders();
		inputHttpHeaders.add("x-api-language", "kr");
		inputHttpHeaders.add("x-api-country", "ko");
		inputHttpHeaders.add("x-api-city", "seoul");
		inputHttpHeaders.add("x-api-day", "1");
		
		Map<String, Object> inputQueryParams = new HashMap<>();

		String requestBody = null;
		//----------------------------------------------------------------------------
		
		ServiceWorkerConfigBase serviceWorkerConfigBase = serviceWorkerConfigManager.getConfig("API-ACCU-WEATHER-V1-FORECAST_DAILY_COMPOSITE");
		if (serviceWorkerConfigBase.getType().equals("composite")) {
			//
			ServiceWorkerConfigComposite serviceWorkerConfigComposite = (ServiceWorkerConfigComposite)serviceWorkerConfigBase;
			
			ServiceWorkerComposite serviceWorker = new ServiceWorkerComposite();
			serviceWorker.addServiceWorkerConfigComposite(serviceWorkerConfigComposite);
			
			Integer index = 0;
			for (String iter : serviceWorkerConfigComposite.getSequence()) {
				serviceWorker.addServiceWorkerConfigSingle(index, (ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig(iter));
				index++;
			}
			
			serviceWorker.setInputs(inputHttpHeaders, inputQueryParams, requestBody);
			
			System.out.println("//---------------------------------------------");
			System.out.println("InputHttpHeaders = " + serviceWorker.getInputHttpHeaders());
			System.out.println("InputQueryParams = " + serviceWorker.getInputQueryParams());
			System.out.println("InputRequestBody = " + serviceWorker.getInputRequestBody());
			System.out.println("DcInputRequestBody = " + serviceWorker.getDcInputRequestBody().jsonString());
			System.out.println("//---------------------------------------------");
			
			serviceWorker.executeWork();
			
			System.out.println("//---------------------------------------------");
			System.out.println("OutgoingRequestBody = " + serviceWorker.getOutgoingRequestBody());
			System.out.println("//---------------------------------------------");
		}
		else if (serviceWorkerConfigBase.getType().equals("single")) {
			// TODO : 
		}
	}
}