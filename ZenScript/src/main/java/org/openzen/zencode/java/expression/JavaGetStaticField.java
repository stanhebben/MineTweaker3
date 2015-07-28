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
public class JavaGetStaticField extends AbstractJavaExpression
{
	private final JavaField field;
	
	public JavaGetStaticField(CodePosition position, IMethodScope<IJavaExpression> scope, JavaField field)
	{
		super(position, scope);
		
		this.field = field;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		method.getStaticField(field.fieldClass, field.fieldName, field.fieldDescriptor);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return field.type;
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
