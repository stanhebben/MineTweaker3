/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.openzen.zencode.java.JavaTypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaGetInstanceField;
import org.openzen.zencode.java.expression.JavaGetStaticField;
import org.openzen.zencode.java.expression.JavaSetInstanceField;
import org.openzen.zencode.java.expression.JavaSetStaticField;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;

/**
 *
 * @author Stan
 */
public class JavaField implements IJavaField
{
	private final Field field;
	private final IJavaType type;

	public JavaField(Field field, JavaTypeCompiler types)
	{
		this(field, types, TypeCapture.<IJavaExpression, IJavaType>empty());
	}

	public JavaField(Field field, JavaTypeCompiler types, TypeCapture<IJavaExpression, IJavaType> capture)
	{
		if (field == null)
			throw new IllegalArgumentException("field cannot be null");

		if (types == null)
			throw new IllegalArgumentException("types cannot be null");

		this.field = field;
		this.type = types.getNativeType(null, field.getGenericType(), capture);
	}

	@Override
	public IJavaType getType()
	{
		return type;
	}

	@Override
	public boolean isFinal()
	{
		return (field.getModifiers() & Modifier.FINAL) > 0;
	}

	@Override
	public boolean isStatic()
	{
		return (field.getModifiers() & Modifier.STATIC) > 0;
	}

	@Override
	public String getInternalClassName()
	{
		return internal(field.getDeclaringClass());
	}

	@Override
	public String getFieldName()
	{
		return field.getName();
	}

	@Override
	public IJavaExpression makeStaticGetExpression(
			CodePosition position,
			IMethodScope<IJavaExpression, IJavaType> scope)
	{
		return new JavaGetStaticField(position, scope, this);
	}

	@Override
	public IJavaExpression makeStaticSetExpression(
			CodePosition position,
			IMethodScope<IJavaExpression, IJavaType> scope,
			IJavaExpression value)
	{
		return new JavaSetStaticField(position, scope, this, value);
	}

	@Override
	public IJavaExpression makeInstanceGetExpression(
			CodePosition position,
			IMethodScope<IJavaExpression, IJavaType> scope,
			IJavaExpression target)
	{
		return new JavaGetInstanceField(position, scope, this, target);
	}

	@Override
	public IJavaExpression makeInstanceSetExpression(
			CodePosition position,
			IMethodScope<IJavaExpression, IJavaType> scope,
			IJavaExpression target, IJavaExpression value)
	{
		return new JavaSetInstanceField(position, scope, this, target, value);
	}
}
