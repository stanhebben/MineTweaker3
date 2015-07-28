/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaInvalid extends AbstractJavaExpression
{
	private final IGenericType<IJavaExpression> type;
	
	public JavaInvalid(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> type)
	{
		super(position, scope);
		
		this.type = type;
	}
	
	@Override
	public void compileValue(MethodOutput method)
	{
		compileStatement(method);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		method.newObject(UnsupportedOperationException.class);
		method.dup();
		method.constant("Attempted to run code that could not be compiled");
		method.construct(UnsupportedOperationException.class, String.class);
		method.aThrow();
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return type;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}

	@Override
	public void validate()
	{
		// already invalid
	}
}
