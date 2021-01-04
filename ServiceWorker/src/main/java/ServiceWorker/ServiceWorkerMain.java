package ServiceWorker;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ServiceWorker.model.seviceWorker.FunctionExp;
import ServiceWorker.model.seviceWorker.JsonUtil;
import ServiceWorker.model.seviceWorker.ServiceWorkerSingle;
import ServiceWorker.model.seviceWorkerConfig.ServiceWorkerConfigSingle;
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
				"    \"scheme\" : \"https\",\r\n" + 
				"	\"host\" : \"www.accumWeather.com\" ,\r\n" + 
				"	\"path\" : \"service/forecastDaily\",\r\n" + 
				"	\"method\" : \"get\",\r\n" + 
				"    \"headers\" : {\r\n" + 
				"      \"key_FD_1\" : [\"FUNCTION( , headers, key_INPUT_1)\"],\r\n" + 
				"      \"key_FD_2\" : [\"FUNCTION( , headers, key_INPUT_2)\"]\r\n" + 
				"    },\r\n" + 
				"    \"queryParams\" : {\r\n" + 
				"      \"key_FD_1\" : \"FUNCTION( , queryParams, key_INPUT_1)\",\r\n" + 
				"      \"key_FD_2\" : \"FUNCTION( , queryParams, key_INPUT_2)\"\r\n" + 
				"    },\r\n" + 
				"    \"body\" : {\r\n" + 
				"      \"code\" : \"FUNCTION( , body, $.code)\",\r\n" + 
				"      \"message\" : \"FUNCTION( , body, $.message)\",\r\n" + 
				"      \"ret\" : {\r\n" + 
				"        \"retKey1\" : \"FUNCTION( , body, $.ret.retKey1)\",\r\n" + 
				"        \"retKey2\" : \"FUNCTION( , body, $.ret.retKey2)\"\r\n" + 
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
		System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY"));
		
		serviceWorkerConfigManager.addConfig(config_composite);
		System.out.println(serviceWorkerConfigManager.getConfigAsString("API-WEATHER-FORECAST_DAILY_HOURLY"));
		
		System.out.println(serviceWorkerConfigManager.getAllConfigs());
		
		Object body = JsonUtil.readValue(config_single, "$.config.body");
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
		
		serviceWorkerSingle.doWork();
		
		System.out.println("OutputHttpHeaders = " + serviceWorkerSingle.getOutputHttpHeaders());
		
		
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