/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.test;

import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.test.expression.TestExpression;
import org.openzen.zencode.test.type.TestType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public abstract class TestMethod implements IMethod<TestExpression, TestType>
{
	private final MethodHeader<TestExpression, TestType> header;
	private final boolean isStatic;
	private final TestType type;
	
	public TestMethod(MethodHeader<TestExpression, TestType> header, boolean isStatic)
	{
		this.header = header;
		this.isStatic = isStatic;
		type = new TestType();
	}
	
	@Override
	public TestExpression callStaticWithConstants(CodePosition position, IScopeMethod<TestExpression, TestType> scope, Object... constantArguments)
	{
		return callStatic(position, scope, Expressions.convert(position, scope, constantArguments));
	}

	@Override
	public TestExpression callVirtualWithConstants(CodePosition position, IScopeMethod<TestExpression, TestType> scope, TestExpression target, Object... constantArguments)
	{
		return callVirtual(position, scope, target, Expressions.convert(position, scope, position));
	}

	@Override
	public boolean isStatic()
	{
		return isStatic;
	}

	@Override
	public TestType getFunctionType()
	{
		return type;
	}

	@Override
	public MethodHeader<TestExpression, TestType> getMethodHeader()
	{
		return header;
	}

	@Override
	public TestType getReturnType()
	{
		return header.getReturnType();
	}
}
