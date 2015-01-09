/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.field.IJavaField;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;

/**
 *
 * @author Stan
 */
public class JavaGetStaticField extends AbstractJavaExpression
{
	private final IJavaField field;
	
	public JavaGetStaticField(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaField field)
	{
		super(position, scope);
		
		this.field = field;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		method.getStaticField(field.getInternalClassName(), field.getFieldName(), field.getType().getSignature());
	}

	@Override
	public IJavaType getType()
	{
		return field.getType();
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}

	@Override
	public void validate()
	{
		
	}
}
