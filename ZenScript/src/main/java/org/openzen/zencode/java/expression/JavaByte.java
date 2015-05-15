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
public class JavaByte extends AbstractJavaExpression
{
	private final byte value;
	
	public JavaByte(CodePosition position, IMethodScope<IJavaExpression> scope, byte value)
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
		return getScope().getTypeCompiler().byte_;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null; // TODO
	}

	@Override
	public void validate()
	{
		
	}
}
