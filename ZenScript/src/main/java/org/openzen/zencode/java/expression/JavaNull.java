/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaNull extends AbstractJavaExpression
{
	public JavaNull(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		super(position, scope);
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult)
			method.aConstNull();
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().null_;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return AnyNull.INSTANCE;
	}

	@Override
	public void validate()
	{
		
	}
}
