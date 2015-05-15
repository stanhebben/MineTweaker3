/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class JavaSetInstanceField extends AbstractJavaExpression
{
	private final JavaField field;
	private final IJavaExpression instance;
	private final IJavaExpression value;

	public JavaSetInstanceField(
			CodePosition position,
			IMethodScope<IJavaExpression> scope,
			JavaField field,
			IJavaExpression instance,
			IJavaExpression value)
	{
		super(position, scope);
		
		this.field = field;
		this.instance = instance;
		this.value = value;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult) {
			value.compile(true, method);
			instance.compile(true, method);
			method.dupX1(value.getType());
			method.putField(field.fieldClass, field.fieldName, field.fieldDescriptor);
		} else {
			instance.compile(true, method);
			value.compile(true, method);
			method.putField(field.fieldClass, field.fieldName, field.fieldDescriptor);
		}
	}

	@Override
	public IGenericType<IJavaExpression> getType()
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
		if (value.getType().equals(field.type))
			throw new AssertionError("Value type != field type");
		
		// TODO: check access to final field outside constructor
	}
}
