/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.field.IJavaField;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 */
public class JavaSetStaticField extends AbstractJavaExpression
{
	private final IJavaField field;
	private final IJavaExpression value;
	
	public JavaSetStaticField(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaField field, IJavaExpression value)
	{
		super(position, scope);
		
		this.field = field;
		this.value = value;
	}
	
	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		value.compile(true, method);
		
		if (pushResult)
			method.dup(field.getType());
		
		method.putStaticField(field.getInternalClassName(), field.getFieldName(), field.getType());
	}

	@Override
	public TypeInstance<IJavaExpression> getType()
	{
		return value.getType();
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return value.getCompileTimeValue();
	}

	@Override
	public void validate()
	{
		if (!value.getType().canCastExplicit(field.getType()))
			getScope().getErrorLogger().errorCannotCastExplicit(getPosition(), value.getType(), field.getType());
		
		// TODO: check assignation to final field
	}
}
