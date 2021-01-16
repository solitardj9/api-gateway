package serviceWorker2.model.function;

import java.util.Map;

import serviceWorker2.model.function.functions.FunctionGet;
import serviceWorker2.model.function.functions.FunctionStringFormat;
import serviceWorker2.model.function.functions.FunctionTrigger;
import serviceWorker2.model.function.functions.FunctionUrlEncode;
import serviceWorker2.model.worker.ServiceWorkerSingle;

public class FunctionFactory {
	
	public enum FunctionKey {
		
		FUNCTION_URLENCODE("FUNCTION_URLENCODE") {

			@Override
			public Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params) {
				//
				return FunctionUrlEncode.executeFuction(serviceWorkerSingle, params);
			}
		},
		FUNCTION_STRINGFORMAT("FUNCTION_STRINGFORMAT") {

			@Override
			public Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params) {
				//
				return FunctionStringFormat.executeFuction(serviceWorkerSingle, params);
			}
		},
		FUNCTION_GET("FUNCTION_GET") {

			@Override
			public Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params) {
				//
				return FunctionGet.executeFuction(serviceWorkerSingle, params);
			}
		},
		FUNCTION_TRIGGER("FUNCTION_TRIGGER") {

			@Override
			public Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params) {
				//
				return FunctionTrigger.executeFuction(serviceWorkerSingle, params);
			}
		}
		;
		
		private String key;
		
		FunctionKey(String key) {
			this.key = key;
		}
		
		@Override
		public String toString() {
			return key;
		}
		
		public abstract Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params);
	}

	public static Object doFuction(ServiceWorkerSingle serviceWorkerSingle, String function, Map<Integer, Object> params) {
		//
		for (FunctionKey iter : FunctionKey.values()) {
			if (iter.toString().equals(function)) {
				return iter.doFuction(serviceWorkerSingle, function, params);
			}
		}
		return null;
	}
}