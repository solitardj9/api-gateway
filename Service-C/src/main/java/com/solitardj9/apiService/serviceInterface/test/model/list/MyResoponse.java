package com.solitardj9.apiService.serviceInterface.test.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResoponse {
	
	private Integer code;
	
	private String message;
	
	private MyListResoponse ret;
}
