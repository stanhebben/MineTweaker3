/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.method.JavaMethods;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class JavaCallVirtual extends AbstractJavaExpression
{
	private final IJavaMethod method;
	private final IJavaExpression target;
	private final IJavaExpression[] arguments;
	
	public JavaCallVirtual(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaMethod method, IJavaExpression target, IJavaExpression... arguments)
	{
		super(position, scope);
		
		this.method = method;
		this.target = target;
		this.arguments = arguments;
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		target.compile(true, output);
		
		for (IJavaExpression argument : arguments)
		{
			argument.compile(true, output);
		}
		
		output.invokeVirtual(
					method.getDeclaringClass(),
					method.getMethodName(),
					JavaMethods.getSignature(method.getMethodHeader()));
		
		if (!result && method.getReturnType() != getScope().getTypes().getVoid())
			output.pop(method.getReturnType().isLarge());
	}

	@Override
	public IJavaType getType()
	{
		return method.getReturnType();
	}
}
