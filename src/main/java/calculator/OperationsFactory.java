package com.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public class OperationsFactory {
	static Map<String, Object> operationsMap = new HashMap();
	
	static {
		operationsMap.put("^", (BiFunction<Integer, Integer, Integer>)Operation.POWER);
		operationsMap.put("*", (BiFunction<Integer, Integer, Integer>)Operation.MULTIPLY);
		operationsMap.put("/", (BiFunction<Integer, Integer, Integer>)Operation.DIVIDE);
		operationsMap.put("+", (BiFunction<Integer, Integer, Integer>)Operation.ADD);
		operationsMap.put("-", (BiFunction<Integer, Integer, Integer>)Operation.SUBSTRACT);
		//If any new operation addition in Operation function need to add entry here
	}

	public static BiFunction<Integer, Integer, Integer> getOperation(String oprFunc) {
		return (BiFunction<Integer, Integer, Integer>)operationsMap.get(oprFunc);
	}
}
