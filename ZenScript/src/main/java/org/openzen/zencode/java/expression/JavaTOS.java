/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 * Special expression to indicate the top of stack.
 * 
 * @author Stan
 */
public class JavaTOS extends AbstractJavaExpression
{
	private final IGenericType<IJavaExpression> type;
	
	public JavaTOS(CodePosition position, IMethodScope<IJavaExpression> scope, IGenericType<IJavaExpression> type)
	{
		super(position, scope);
		
		this.type = type;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (!pushResult)
			method.pop(type);
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
		
	}
}
