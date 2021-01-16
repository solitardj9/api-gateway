package com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function;

import com.solitardj9.apiService.applicationInterface.service.service.impl.serviceWorker.model.function.exp.FunctionExp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionExpResponse {
	
	private FunctionExp functionExp;
	
	private Integer returnedIndex;
}
