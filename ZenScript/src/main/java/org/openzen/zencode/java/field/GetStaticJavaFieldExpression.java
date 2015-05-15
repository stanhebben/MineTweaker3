/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.field;

import java.lang.reflect.Field;
import org.openzen.zencode.java.expression.AbstractJavaExpression;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class GetStaticJavaFieldExpression extends AbstractJavaExpression
{
	private final Field field;
	private final IGenericType<IJavaExpression> type;
	
	public GetStaticJavaFieldExpression(CodePosition position, IMethodScope<IJavaExpression> scope, Field field, IGenericType<IJavaExpression> type)
	{
		super(position, scope);
		
		this.field = field;
		this.type = type;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult)
			method.getField(field.getDeclaringClass(), field.getName(), field.getType());
	}

	@Override
	public IJavaExpression assign(CodePosition position, IJavaExpression other)
	{
		return new SetStaticJavaFieldExpression(position, getScope(), field, type, other);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return type;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		// TODO: we can do this.. maybe
		return null;
	}

	@Override
	public void validate()
	{
		// nothing to do
	}
}
