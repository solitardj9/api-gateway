package serviceWorker2.model.function.functions;

import java.util.Map;

import serviceWorker2.model.worker.ServiceWorkerSingle;

public class FunctionStringFormat {

	public static String executeFuction(ServiceWorkerSingle serviceWorkerSingle, Map<Integer, Object> params) {
		// 
		String string = (String)params.get(1);
		
		for (Integer i = 1 ; i < params.size() ; i++) {
			String strIndex = "{" + i + "}";
			//System.out.println("[FunctionStringFormat].executeFuction : strIndex = " + strIndex);
			string = string.replace(strIndex, params.get(i + 1).toString());
			//System.out.println("[FunctionStringFormat].executeFuction : string = " + string);
		}
		//System.out.println("[FunctionStringFormat].executeFuction : string = " + string);
		return string; 
	}
}
