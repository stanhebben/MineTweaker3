/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import java.util.Arrays;
import org.objectweb.asm.Label;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 */
public class JavaCallStaticNullable extends AbstractJavaExpression
{
	private final IJavaMethod method;
	private final IJavaExpression value;
	
	public JavaCallStaticNullable(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			IJavaMethod method,
			IJavaExpression value)
	{
		super(position, scope);
		
		this.method = method;
		this.value = value;
	}
	
	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		value.compile(true, method);
		
		Label lblNotNull = new Label();
		Label lblAfter = new Label();
		
		method.dup();
		method.ifNonNull(lblNotNull);
		method.pop();
		method.aConstNull();
		method.goTo(lblAfter);

		method.label(lblNotNull);
		
		IJavaExpression expression = this.method.callStatic(
				getPosition(),
				getScope(),
				Arrays.<IJavaExpression>asList(new JavaTOS(getPosition(), getScope(), value.getType())));
		expression.compile(true, method);

		method.label(lblAfter);
	}

	@Override
	public TypeInstance<IJavaExpression> getType()
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
		method.validateCall(getPosition(), getScope(), Arrays.asList(value));
	}
}
