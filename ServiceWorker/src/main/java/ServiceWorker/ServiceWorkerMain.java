package ServiceWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorker.FunctionExp;
import ServiceWorker.model.seviceWorker.JsonUtil;
import ServiceWorker.model.seviceWorker.ServiceWorkerSingle;
import ServiceWorker.service.ServiceWorkerConfigManager;

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
				"  \"name\" : \"API-WEATHER-FORECAST_DAILY\",\r\n" + 
				"  \"type\" : \"single\",\r\n" + 
				"  \"config\" : {\r\n" + 
				"    \"scheme\" : \"http\",\r\n" + 
				"	\"host\" : \"127.0.0.1:19370\" ,\r\n" + 
				"	\"path\" : \"service-a/test/getList\",\r\n" + 
				"	\"method\" : \"PUT\",\r\n" + 
				"    \"headers\" : {\r\n" + 
				"      \"key_FD_1\" : [\"FUNCTION( , headers, key_INPUT_1)\"],\r\n" + 
				"      \"key_FD_2\" : [\"FUNCTION( , headers, key_INPUT_2)\"]\r\n" + 
				"    },\r\n" + 
				"    \"queryParams\" : {\r\n" + 
				"      \"key_FD_1\" : \"FUNCTION( , queryParams, key_INPUT_1)\",\r\n" + 
				"      \"key_FD_2\" : \"FUNCTION( , queryParams, key_INPUT_2)\"\r\n" + 
				"    },\r\n" + 
				"	 \"requestBody\" : {\r\n" +
				"	   \"id\" : \"FUNCTION( , requestBody, $.id)\",\r\n" + 
				"      \"age\" : \"FUNCTION( , requestBody, $.age)\"\r\n" + 
				"	 },\r\n" +
				"    \"responseBody\" : {\r\n" +
				"      \"code\" : \"FUNCTION( , responseBody, $.code)\",\r\n" +
				"      \"message\" : \"FUNCTION( , responseBody, $.message)\",\r\n" +
				"      \"ret\" : {\r\n" +
				"        \"retKey1\" : \"FUNCTION( , responseBody, $.ret.retKey1)\",\r\n" +
				"        \"retKey2\" : \"FUNCTION( , responseBody, $.ret.retKey2)\"\r\n" +
				"      }\r\n" +
				"    }" + 
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
		System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY"));
		
		serviceWorkerConfigManager.addConfig(config_composite);
		System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY_HOURLY"));
		
		System.out.println(serviceWorkerConfigManager.getAllConfigs());
		
		Object body = JsonUtil.readValue(config_single, "$.config.responseBody");
		System.out.println(body);
		Object code = JsonUtil.readValue(body, "$.code");
		System.out.println(code);
		
//		ServiceWorkerConfigSingle serviceWorkerConfigSingle = (ServiceWorkerConfigSingle)serviceWorkerConfigManager.getConfig("API-WEATHER-FORECAST_DAILY");
//		Map<String, List<String>> headers = serviceWorkerConfigSingle.getConfig().getHeaders();
//		for (Entry<String, List<String>> iterHeaders : headers.entrySet()) {
//			List<String> headerValues = iterHeaders.getValue();
//			for (String iterHeaderValue : headerValues) {
//				executeFunction(iterHeaderValue);
//			}
//		}
		
		ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY"));
		
		HttpHeaders inputHttpHeaders = new HttpHeaders();
		inputHttpHeaders.add("key_INPUT_1", "Aaa");
		inputHttpHeaders.add("key_INPUT_2", "Bbb");
		System.out.println("inputHttpHeaders = " + inputHttpHeaders);
		serviceWorkerSingle.setInputHttpHeaders(inputHttpHeaders);
		
		Map<String, Object> inputQueryParams = new HashMap<>();
		inputQueryParams.put("key_INPUT_1", "deviceId_test");
		inputQueryParams.put("key_INPUT_2", 35.5);
		System.out.println("inputHttpHeaders = " + inputHttpHeaders);
		serviceWorkerSingle.setInputQueryParams(inputQueryParams);
		
		MyListRequest myListRequest = new MyListRequest("bk.cha", 21);
		try {
			serviceWorkerSingle.setInputRequestBody(om.writeValueAsString(myListRequest));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		serviceWorkerSingle.doWork();
		System.out.println("//---------------------------------------------");
		System.out.println("OutputHttpHeaders = " + serviceWorkerSingle.getOutgoingHttpHeaders());
		System.out.println("OutputQueryParams = " + serviceWorkerSingle.getOutgoingQueryParams());
		System.out.println("OutgoingRequestBody = " + serviceWorkerSingle.getOutgoingRequestBody());
		System.out.println("InputResponseBody = " + serviceWorkerSingle.getInputResponseBody());
		System.out.println("//---------------------------------------------");
	}
	
	private static Object executeFunction(String str) {
		//
		String tmpStr = new String(str);
		
		String regExp = "FUNCTION\\((.*?)\\)";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(tmpStr);
		
		while (m.find()) {
			//
			String matchedExp = m.group();
			
			System.out.println("matchedExp = " + matchedExp);
			
			FunctionExp functionExp = extractFunction(matchedExp);
			System.out.println("functionExp = " + functionExp.toString());
		}
		
		return null;
	}
	
	private static FunctionExp extractFunction(String fuction) {
		//
		FunctionExp functionExp = new FunctionExp();
				
		String tmpFunction = new String(fuction);
		tmpFunction = tmpFunction.replace("FUNCTION", "").replace("(", "").replace(")", "");
		
		Integer index = 0;
		StringTokenizer stringTokenizer = new StringTokenizer(tmpFunction, ",");
		while(stringTokenizer.hasMoreTokens()){
			functionExp.setValue(index, stringTokenizer.nextToken());
			index++;
		}
		return functionExp;
	}
	
}