/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaBool extends AbstractJavaExpression
{
	private final boolean value;
	
	public JavaBool(CodePosition position, IMethodScope<IJavaExpression> scope, boolean value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		method.constant(value);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().bool;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return AnyBool.valueOf(value);
	}

	@Override
	public void validate()
	{
		
	}
}
