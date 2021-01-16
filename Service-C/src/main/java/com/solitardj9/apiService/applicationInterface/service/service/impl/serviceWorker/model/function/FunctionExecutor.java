package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function.exp.FunctionExp;
import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.worker.ServiceWorkerSingle;

import java.util.StringTokenizer;

public class FunctionExecutor {
	
	private static final String PREFIX_FUNCTION = "FUNCTION_";
	
	public static Object executeFunctions(ServiceWorkerSingle serviceWorkerSingle, String expressions) {
		//
		List<String> words = stringTokenizeFunctionExps(expressions);
//		System.out.println("[FunctionExecutor].executeFunctions : words = " + words.toString());

		FunctionExp functionExp = makeFunctionExps(words, 0).getFunctionExp();
//		System.out.println("[FunctionExecutor].executeFunctions : functionExp = " + functionExp.toString());
		
		return executeFunctionExps(serviceWorkerSingle, functionExp);
	}
	
	private static List<String> stringTokenizeFunctionExps(String expressions) {
		//
		List<String> words = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(expressions, ",()", true);
		while(stringTokenizer.hasMoreTokens()){
			words.add(stringTokenizer.nextToken().trim());
		}
		return words;
	}
	
	private static FunctionExpResponse makeFunctionExps(List<String> words, Integer startIndex) {
		//
		Boolean isStart = false;
		Boolean isEnd = false;
		Integer index = 0;
		FunctionExp functionExp = null;
				
		for (Integer i = startIndex ; i < words.size() ; i++) {
			String iter = words.get(i);
			if (isStart.equals(false)) {
				if (index.equals(0) && iter.startsWith(PREFIX_FUNCTION)) {
					isStart = true;
					functionExp = new FunctionExp();
					functionExp.setFunction(iter);
					index++;
				}
				else {
					return null;
				}
			}
			else {
				if (!iter.startsWith(PREFIX_FUNCTION)) {
					if ( (!iter.equals("(")) && (!iter.equals(",")) && (!iter.equals(")")) ) {
						functionExp.setParam(index, iter);
						index++;
					}
					else if (iter.equals(")")) {
						isEnd = true;
						startIndex = i;
						break;
					}
				}
				else {
					FunctionExpResponse functionExpResponse = makeFunctionExps(words, i);
					functionExp.setParam(index, functionExpResponse.getFunctionExp());
					i = functionExpResponse.getReturnedIndex();
					index++;
				}
			}
		}
		
		if (isEnd)
			return new FunctionExpResponse(functionExp, startIndex);
		else
			return null;
	}

	private static Object executeFunctionExps(ServiceWorkerSingle serviceWorkerSingle, FunctionExp functionExp) {
		// 
//		System.out.println("[FunctionExecutor].executeFunctionExps : start");
		String function = functionExp.getFunction();
		Map<Integer, Object> params = functionExp.getParams();
//		System.out.println("[FunctionExecutor].executeFunctionExps : b/f params = " + params.toString());
		for (Entry<Integer, Object> iter : params.entrySet()) {
			if (iter.getValue() instanceof FunctionExp) {
				params.put(iter.getKey(), executeFunctionExps(serviceWorkerSingle, (FunctionExp)iter.getValue()));
			}
		}
//		System.out.println("[FunctionExecutor].executeFunctionExps : a/f params = " + params.toString());
		Object retObject = FunctionFactory.doFuction(serviceWorkerSingle, function, params);
		if (retObject != null) {
//			System.out.println("[FunctionExecutor].executeFunctionExps : retObject = " + retObject.toString());
//			System.out.println("[FunctionExecutor].executeFunctionExps : retObject class = " + retObject.getClass().getSimpleName());
		}
		else {
//			System.out.println("[FunctionExecutor].executeFunctionExps : retObject = null");
//			System.out.println("[FunctionExecutor].executeFunctionExps : retObject class = null");
		}
		
		return retObject;
	}
}