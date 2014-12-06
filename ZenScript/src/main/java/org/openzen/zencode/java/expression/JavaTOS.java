/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;

/**
 * Special expression to indicate the top of stack.
 * 
 * @author Stan
 */
public class JavaTOS extends AbstractJavaExpression
{
	private final IJavaType type;
	
	public JavaTOS(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaType type)
	{
		super(position, scope);
		
		this.type = type;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (!pushResult)
			method.pop(type.isLarge());
	}

	@Override
	public IJavaType getType()
	{
		return type;
	}
}
