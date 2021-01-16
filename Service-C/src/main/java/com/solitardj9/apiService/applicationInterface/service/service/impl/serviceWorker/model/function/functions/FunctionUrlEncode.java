package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.worker.ServiceWorkerSingle;

public class FunctionUrlEncode extends Function {
	
	public static String executeFuction(ServiceWorkerSingle serviceWorkerSingle, Map<Integer, Object> params) {
		// 
		String string = null;
		if (params.get(1) instanceof String)
			string  = (String)params.get(1);
		else
			string = params.get(1).toString();
		
		String encoder = (String)params.get(2);
		
		try {
			return URLEncoder.encode(string, encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
	}
}
