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
public class JavaString extends AbstractJavaExpression
{
	private final String value;
	
	public JavaString(CodePosition position, IMethodScope<IJavaExpression> scope, String value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		method.constant(value);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().string;
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
