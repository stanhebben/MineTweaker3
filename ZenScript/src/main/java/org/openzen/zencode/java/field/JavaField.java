/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.field;

import java.lang.reflect.Field;
import org.openzen.zencode.java.expression.IJavaExpression;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class JavaField
{
	public final String fieldClass;
	public final String fieldName;
	public final String fieldDescriptor;
	public final IGenericType<IJavaExpression> type;
	
	public JavaField(String fieldClass, String fieldName, String fieldDescriptor, IGenericType<IJavaExpression> type)
	{
		this.fieldClass = fieldClass;
		this.fieldName = fieldName;
		this.fieldDescriptor = fieldDescriptor;
		this.type = type;
	}
	
	public JavaField(Field field, IGenericType<IJavaExpression> type)
	{
		this.fieldClass = internal(field.getDeclaringClass());
		this.fieldName = field.getName();
		this.fieldDescriptor = internal(field.getType());
		this.type = type;
	}
}
