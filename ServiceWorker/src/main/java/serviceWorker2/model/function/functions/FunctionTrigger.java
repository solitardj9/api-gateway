package serviceWorker2.model.function.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import serviceWorker2.model.utils.JsonUtil;
import serviceWorker2.model.worker.ServiceWorkerSingle;

public class FunctionTrigger extends Function {

	public static Boolean executeFuction(ServiceWorkerSingle serviceWorkerSingle, Map<Integer, Object> params) {
		//
		Boolean ret = false;
		
		String resource = (String)params.get(1);
		String key = (String)params.get(2);
		String value = (String)params.get(3);
		
		if (resource.equals("headers")) {
			ret = compareHttpHeaders(serviceWorkerSingle, key, value);
			//System.out.println("[ServiceWorkerSingle].doTrigger : headers = " + ret);
		}
		else if (resource.equals("queryParams")) {
			ret = compareQueryParams(serviceWorkerSingle, key, value);
			//System.out.println("[ServiceWorkerSingle].doTrigger : queryParams = " + ret);
		}
		else if (resource.equals("requestBody")) {
			ret = compareRequestBody(serviceWorkerSingle, key, value);
			//System.out.println("[ServiceWorkerSingle].doTrigger : requestBody = " + ret);
		}
		else if (resource.equals("responseBody")) {
			ret = compareResponseBody(serviceWorkerSingle, key, value);
			//System.out.println("[ServiceWorkerSingle].doTrigger : responseBody = " + ret);
		}
		else if (resource.equals("responseStatus")) {
			ret = compareResponseStatus(serviceWorkerSingle, key, value);
			//System.out.println("[ServiceWorkerSingle].doTrigger : responseStatus = " + ret);
		}
		else {
			ret = false;
		}
		
		return ret;
	}
	
	private static Boolean compareHttpHeaders(ServiceWorkerSingle serviceWorkerSingle, String key, String value) {
		//
		List<String> list = extractHeaderValues(value);
		return serviceWorkerSingle.getInputHttpHeaders().get(key).containsAll(list);
	}
	
	private static List<String> extractHeaderValues(String headerValues) {
		//
		List<String> values = new ArrayList<>();
				
		String tmpTrigger = new String(headerValues);
		tmpTrigger = tmpTrigger.replace("[", "").replace("]", "");
		
		StringTokenizer stringTokenizer = new StringTokenizer(tmpTrigger, ",");
		while(stringTokenizer.hasMoreTokens()){
			values.add(stringTokenizer.nextToken().strip());
		}
		return values;
	}
	
	private static Boolean compareQueryParams(ServiceWorkerSingle serviceWorkerSingle, String key, String value) {
		//
		try {
			if (serviceWorkerSingle.getInputQueryParams().containsKey(key)) {
				if (serviceWorkerSingle.getInputQueryParams().get(key).toString().equals(value))
					return true;
				return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static Boolean compareRequestBody(ServiceWorkerSingle serviceWorkerSingle, String key, String value) {
		// 
		try {
			//Object object = JsonUtil.readValue(serviceWorkerSingle.getInputRequestBody(), key);
			Object object = JsonUtil.readValue(serviceWorkerSingle.getDcInputRequestBody(), key);
			if (value != null) {
				if(object.toString().equals(value))
					return true;
				else
					return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static Boolean compareResponseBody(ServiceWorkerSingle serviceWorkerSingle, String key, String value) {
		//
		try {
			//Object object = JsonUtil.readValue(serviceWorkerSingle.getInputResponseBody(), key);
			Object object = JsonUtil.readValue(serviceWorkerSingle.getDcInputResponseBody(), key);
			if (object != null) {
				if(object.toString().equals(value))
					return true;
				else
					return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static Boolean compareResponseStatus(ServiceWorkerSingle serviceWorkerSingle, String key, String value) {
		//
		try {
			if (serviceWorkerSingle.getInputResponseEntity().getStatusCode().value() == Integer.valueOf(value))
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}