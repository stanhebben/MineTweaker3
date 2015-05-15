/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.annotations;

import static org.openzen.zencode.annotations.OperatorType.*;

/**
 * Used to indicate comparison types.
 * 
 * @author Stan Hebben
 */
public enum CompareType {
	LT(LESS, GREATER_EQUALS),
	GT(GREATER, LESS_EQUALS),
	EQ(EQUALS, NOTEQUALS),
	NE(NOTEQUALS, EQUALS),
	LE(LESS_EQUALS, GREATER),
	GE(GREATER_EQUALS, LESS);
	
	public final OperatorType operator;
	public final OperatorType inverseOperator;
	
	private CompareType(OperatorType operator, OperatorType inverseOperator)
	{
		this.operator = operator;
		this.inverseOperator = inverseOperator;
	}
	
	public boolean evaluate(int value)
	{
		switch (this) {
			case LT: return value < 0;
			case GT: return value > 0;
			case EQ: return value == 0;
			case NE: return value != 0;
			case LE: return value <= 0;
			case GE: return value >= 0;
			default:
				throw new AssertionError("Missing case");
		}
	}
}
