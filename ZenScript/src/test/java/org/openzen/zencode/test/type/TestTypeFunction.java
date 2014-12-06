/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.test.type;

import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.test.expression.TestExpression;

/**
 *
 * @author Stan
 */
public class TestTypeFunction extends TestType
{
	private final MethodHeader<TestExpression, TestType> header;
	
	public TestTypeFunction(MethodHeader<TestExpression, TestType> header)
	{
		this.header = header;
	}

	@Override
	public MethodHeader<TestExpression, TestType> getFunctionHeader()
	{
		return header;
	}
}
