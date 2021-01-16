package serviceWorker2.model.function.functions;

import java.util.Map;

import serviceWorker2.model.worker.ServiceWorkerSingle;

public class FunctionGet extends Function {

	public static Object executeFuction(ServiceWorkerSingle serviceWorkerSingle, Map<Integer, Object> params) {
		//
		String api = (String)params.get(1);
		String resource = (String)params.get(2);
		String key = (String)params.get(3);
		Integer headerReference = null;
		if (params.containsKey(4))
			headerReference = Integer.valueOf(params.get(4).toString());
		
		if (api == null || api.isEmpty()) {
			if (resource.equals("headers")) {
				return getValueOfInputHttpHeader(serviceWorkerSingle, key, headerReference);
			}
			else if (resource.equals("queryParams")) {
				return getValueOfInputQueryParams(serviceWorkerSingle, key);
			}
			else if (resource.equals("requestBody")) {
				return getValueOfInputRequestBody(serviceWorkerSingle, key);
			}
			else if (resource.equals("responseBody")) {
				return getValueOfInputResponseBody(serviceWorkerSingle, key);
			}
			else if (resource.equals("responseStatus")) {
				return getValueOfResponseStatus(serviceWorkerSingle, key);	
			}
			else {
				return null;
			}
		}
		else {
			if (api.toUpperCase().equals("COMPOSITE")) {
				if (resource.equals("headers")) {
					return getValueOfHttpHeaderFromComposite(serviceWorkerSingle, key, headerReference);
				}
				else if (resource.equals("queryParams")) {
					return getValueOfQueryParamsFromComposite(serviceWorkerSingle, key);
				}
				else if (resource.equals("requestBody")) {
					return getValueOfRequestBodyFromComposite(serviceWorkerSingle, key);
				}
				else {
					return null;
				}
			}
			else {
				if (resource.equals("headers")) {
					return getValueOfHttpHeaderFromOhterApi(serviceWorkerSingle, api, key, headerReference);
				}
				else if (resource.equals("queryParams")) {
					return getValueOfQueryParamsFromOhterApi(serviceWorkerSingle, api, key);
				}
				else if (resource.equals("requestBody")) {
					return getValueOfRequestBodyFromOhterApi(serviceWorkerSingle, api, key);
				}
				else if (resource.equals("responseBody")) {
					return getValueOfResponseBodyFromOhterApi(serviceWorkerSingle, api, key);
				}
				else if (resource.equals("responseStatus")) {
					return getValueOfResponseStatusFromOhterApi(serviceWorkerSingle, api, key);
				}
				else {
					return null;
				}
			}
		}
	}
	
	// Inner Interface --------------------------------------------------------------------------------------------------
	// Http Header --------------------------
	private static Object getValueOfInputHttpHeader(ServiceWorkerSingle serviceWorkerSingle, String key, Integer headerReference) {
		// read value of specific key path in inputHttpHeader
		return serviceWorkerSingle.getValueOfInputHttpHeader(key, headerReference);
	}
	
	private static Object getValueOfHttpHeaderFromOhterApi(ServiceWorkerSingle serviceWorkerSingle, String api, String key, Integer headerReference) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfHttpHeaderFromOhterApi(api, key, headerReference);
	}
	
	private static Object getValueOfHttpHeaderFromComposite(ServiceWorkerSingle serviceWorkerSingle, String key, Integer headerReference) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfInputHttpHeader(key, headerReference);
	}
	
	// Query Parameters ---------------------
	private static Object getValueOfInputQueryParams(ServiceWorkerSingle serviceWorkerSingle, String key) {
		// read value of specific key path in inputHttpHeader
		return serviceWorkerSingle.getValueOfInputQueryParams(key);
	}
	
	private static Object getValueOfQueryParamsFromOhterApi(ServiceWorkerSingle serviceWorkerSingle, String api, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfQueryParamsFromOhterApi(api, key);
	}
	
	private static Object getValueOfQueryParamsFromComposite(ServiceWorkerSingle serviceWorkerSingle, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfInputQueryParams(key);
	}
	
	// Request Body ---------------------------------
	private static Object getValueOfInputRequestBody(ServiceWorkerSingle serviceWorkerSingle, String key) {
		//
		// read value of specific key path in inputRequestBody
		return serviceWorkerSingle.getValueOfInputRequestBody(key);
	}
	
	private static Object getValueOfRequestBodyFromOhterApi(ServiceWorkerSingle serviceWorkerSingle, String api, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfRequestBodyFromOhterApi(api, key);
	}
	
	private static Object getValueOfRequestBodyFromComposite(ServiceWorkerSingle serviceWorkerSingle, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfInputRequestBody(key);
	}
	
	// Response Body ---------------------------------
	private static Object getValueOfInputResponseBody(ServiceWorkerSingle serviceWorkerSingle, String key) {
		//
		// read value of specific key path in inputRequestBody
		return serviceWorkerSingle.getValueOfInputResponseBody(key);
	}
	
	private static Object getValueOfResponseBodyFromOhterApi(ServiceWorkerSingle serviceWorkerSingle, String api, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfResponseBodyFromOhterApi(api, key);
	}
	
	// Response Status ----------------------
	private static Object getValueOfResponseStatus(ServiceWorkerSingle serviceWorkerSingle, String key) {
		//
		return serviceWorkerSingle.getValueOfResponseStatus(key);
	}
	
	private static Object getValueOfResponseStatusFromOhterApi(ServiceWorkerSingle serviceWorkerSingle, String api, String key) {
		//
		return serviceWorkerSingle.getServiceWorkerComposite().getValueOfResponseStatusFromOhterApi(api, key);
	}
	//-------------------------------------------------------------------------------------------------------------------
}