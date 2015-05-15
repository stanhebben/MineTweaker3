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
	ADDASSIGN(2, "+="),
	SUB(2, "-"),
	SUBASSIGN(2, "-="),
	MUL(2, "*"),
	MULASSIGN(2, "*="),
	DIV(2, "/"),
	DIVASSIGN(2, "/="),
	MOD(2, "%"),
	MODASSIGN(2, "%="),
	CAT(2, "~"),
	CATASSIGN(2, "~="),
	OR(2, "|"),
	ORASSIGN(2, "|="),
	AND(2, "&"),
	ANDASSIGN(2, "&="),
	XOR(2, "^"),
	XORASSIGN(2, "^="),
	INVERT(1, "~"),
	NEG(1, "-"),
	NOT(1, "!"),
	SHL(2, "<<"),
	SHR(2, ">>"),
	SHLASSIGN(2, "<<="),
	SHRASSIGN(2, ">>="),
	INDEXGET(2, "[]"),
	INDEXSET(3, "[]="),
	RANGE(2, ".."),
	CONTAINS(2, "in"),
	COMPARE(2, "compare"),
	MEMBERGETTER(3, "member get"),
	MEMBERSETTER(3, "member set"),
	MEMBERCALLER(3, "member caller"),
	EQUALS(2, "=="),
	NOTEQUALS(2, "!="),
	FOR(1, "for"),
	GREATER(2, ">"),
	GREATER_EQUALS(2, ">="),
	LESS(2, "<"),
	LESS_EQUALS(2, "<=");
	
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
