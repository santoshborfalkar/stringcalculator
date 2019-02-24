package com.calculator;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public interface Operation {

	static BinaryOperator<Integer> POWER = (a, b) -> (int)Math.pow(a, b);
	static BinaryOperator<Integer> DIVIDE = (a, b) -> a/b;
	static BinaryOperator<Integer> MULTIPLY = (a, b) -> a*b;
	static BinaryOperator<Integer> ADD = (a, b) -> a+b;
	static BinaryOperator<Integer> SUBSTRACT = (a, b) -> a-b;
	//If any new operation add function above

	public int apply(int operand1, int operand2, BiFunction oprFunction);
}
