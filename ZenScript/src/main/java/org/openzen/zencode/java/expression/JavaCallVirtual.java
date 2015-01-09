/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import java.util.List;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.method.JavaMethods;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;

/**
 *
 * @author Stan
 */
public class JavaCallVirtual extends AbstractJavaExpression
{
	private final IJavaMethod method;
	private final IJavaExpression target;
	private final List<IJavaExpression> arguments;
	
	public JavaCallVirtual(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaMethod method, IJavaExpression target, List<IJavaExpression> arguments)
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
		
		if (!result && method.getReturnType() != getScope().getTypeCompiler().getVoid(getScope()))
			output.pop(method.getReturnType().isLarge());
	}

	@Override
	public IJavaType getType()
	{
		return method.getReturnType();
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}

	@Override
	public void validate()
	{
		method.validateCall(getPosition(), getScope(), arguments);
	}
}
