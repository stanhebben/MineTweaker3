/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import java.util.Collections;
import org.objectweb.asm.Label;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;

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
	public void compileValue(MethodOutput output)
	{
		value.compileValue(output);
		
		Label lblNotNull = new Label();
		Label lblAfter = new Label();
		
		output.dup();
		output.ifNonNull(lblNotNull);
		output.pop();
		output.aConstNull();
		output.goTo(lblAfter);

		output.label(lblNotNull);
		
		output.invokeStatic(method.getDeclaringClass(), method.getMethodName(), method.getMethodSignature());

		output.label(lblAfter);
	}
	
	@Override
	public void compileStatement(MethodOutput output)
	{
		value.compileValue(output);
		
		Label lblNotNull = new Label();
		Label lblAfter = new Label();
		
		output.dup();
		output.ifNonNull(lblNotNull);
		output.pop();
		output.goTo(lblAfter);

		output.label(lblNotNull);
		
		output.invokeStatic(method.getDeclaringClass(), method.getMethodName(), method.getMethodSignature());
		if (method.getReturnType() != getScope().getTypeCompiler().void_)
			output.pop(method.getReturnType());
		
		output.label(lblAfter);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
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
		if (!method.accepts(Collections.singletonList(value)))
			getScope().getErrorLogger().errorInvalidMethodCall(getPosition(), method.getMethodName(), Collections.singletonList(value));
	}
}
