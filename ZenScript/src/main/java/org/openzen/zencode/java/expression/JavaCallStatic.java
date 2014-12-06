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
public class JavaCallStatic extends AbstractJavaExpression
{
	private final IJavaMethod method;
	private final IJavaExpression[] arguments;
	
	public JavaCallStatic(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaMethod method, IJavaExpression... arguments)
	{
		super(position, scope);
		
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput output)
	{
		for (IJavaExpression argument : arguments)
		{
			argument.compile(true, output);
		}
		
		output.invokeStatic(
					method.getDeclaringClass(),
					method.getMethodName(),
					JavaMethods.getSignature(method.getMethodHeader()));
		
		if (!pushResult && method.getReturnType() != getScope().getTypes().getVoid())
			output.pop(method.getReturnType().isLarge());
	}

	@Override
	public IJavaType getType()
	{
		return method.getReturnType();
	}
}
