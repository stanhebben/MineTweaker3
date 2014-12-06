/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.annotations;

/**
 * Enum used to indicate operator type.
 * 
 * @author Stan Hebben
 */
public enum OperatorType {
	ADD(2, "+"),
	SUB(2, "-"),
	MUL(2, "*"),
	DIV(2, "/"),
	MOD(2, "%"),
	CAT(2, "~"),
	OR(2, "|"),
	AND(2, "&"),
	XOR(2, "^"),
	INVERT(1, "~"),
	NEG(1, "-"),
	NOT(1, "!"),
	INDEXSET(3, "[]"),
	INDEXGET(2, "[]="),
	RANGE(2, ".."),
	CONTAINS(2, "in"),
	COMPARE(2, "compare"),
	MEMBERGETTER(3, "member get"),
	MEMBERSETTER(3, "member set"),
	EQUALS(2, "==");
	
	private final int arguments;
	private final String operator;
	
	private OperatorType(int arguments, String operator) {
		this.arguments = arguments;
		this.operator = operator;
	}
	
	public String getOperatorString() {
		return operator;
	}
	
	public int getArgumentCount() {
		return arguments;
	}
	
	public boolean isUnary() {
		return arguments == 1;
	}
}
