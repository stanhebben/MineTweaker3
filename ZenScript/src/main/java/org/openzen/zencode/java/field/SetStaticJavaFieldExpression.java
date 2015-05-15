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
public class SetStaticJavaFieldExpression extends AbstractJavaExpression
{
	private final Field field;
	private final IGenericType<IJavaExpression> type;
	private final IJavaExpression value;
	
	public SetStaticJavaFieldExpression(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			Field field,
			IGenericType<IJavaExpression> type,
			IJavaExpression value)
	{
		super(position, scope);
		
		this.field = field;
		this.type = type;
		this.value = value;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		value.compile(true, method);
		if (pushResult)
			method.dup(type);
		
		method.putField(field.getDeclaringClass(), field.getName(), field.getType());
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
		if (!type.equals(value.getType()))
			throw new RuntimeException("Types don't match");
	}
}
