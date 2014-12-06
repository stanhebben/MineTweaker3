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
public class JavaSetStaticField extends AbstractJavaExpression
{
	private final IJavaField field;
	private final IJavaExpression value;
	
	public JavaSetStaticField(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaField field, IJavaExpression value)
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
			method.dup(field.getType().isLarge());
		
		method.putStaticField(field.getInternalClassName(), field.getFieldName(), field.getType().getSignature());
	}

	@Override
	public IJavaType getType()
	{
		return value.getType();
	}
}
