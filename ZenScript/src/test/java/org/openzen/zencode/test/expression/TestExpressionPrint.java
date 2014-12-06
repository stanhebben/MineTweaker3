/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.test.expression;

import org.openzen.zencode.runtime.AnyString;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.test.type.TestType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TestExpressionPrint extends TestExpression
{
	private final String value;
	
	public TestExpressionPrint(CodePosition position, IScopeMethod<TestExpression, TestType> scope, String value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public IAny getValue()
	{
		return new AnyString(value);
	}

	@Override
	public TestType getType()
	{
		return getScope().getTypes().getString();
	}
}
