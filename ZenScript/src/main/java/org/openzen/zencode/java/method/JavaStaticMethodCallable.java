/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.method;

import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaStaticMethodCallable implements ICallable<IJavaExpression>
{
	private final IJavaMethod method;
	private final InstancedMethodHeader<IJavaExpression> header;
	
	public JavaStaticMethodCallable(IJavaMethod method, InstancedMethodHeader<IJavaExpression> header)
	{
		this.method = method;
		this.header = header;
	}

	@Override
	public IJavaExpression call(CodePosition position, IMethodScope<IJavaExpression> scope, List<IJavaExpression> arguments)
	{
		return new JavaCallStatic(position, scope, method, arguments);
	}

	@Override
	public IJavaExpression callWithConstants(CodePosition position, IMethodScope<IJavaExpression> scope, Object... values)
	{
		return call(position, scope, Expressions.convert(position, scope, values));
	}

	@Override
	public String getFullName()
	{
		return method.getMethodName();
	}

	@Override
	public InstancedMethodHeader<IJavaExpression> getMethodHeader()
	{
		return header;
	}

	@Override
	public IJavaExpression asValue(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		// TODO: implement
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
