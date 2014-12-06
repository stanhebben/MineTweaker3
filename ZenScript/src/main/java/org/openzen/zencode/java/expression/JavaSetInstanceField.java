/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.field.IJavaField;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class JavaSetInstanceField extends AbstractJavaExpression
{
	private final IJavaField field;
	private final IJavaExpression instance;
	private final IJavaExpression value;

	public JavaSetInstanceField(
			CodePosition position,
			IScopeMethod<IJavaExpression, IJavaType> scope,
			IJavaField field,
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
			method.dupX1(value.getType().isLarge());
			method.putField(field.getInternalClassName(), field.getFieldName(), field.getType().getSignature());
		} else {
			instance.compile(true, method);
			value.compile(true, method);
			method.putField(field.getInternalClassName(), field.getFieldName(), field.getType().getSignature());
		}
	}

	@Override
	public IJavaType getType()
	{
		return value.getType();
	}
}
