/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.type.JavaTypeInfo;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;

/**
 *
 * @author Stan
 */
public class JavaCompileState
{
	private final Map<ITypeDefinition<IJavaExpression>, JavaTypeInfo> typeInfo;
	private final Map<IMember<IJavaExpression>, IJavaMethod> methodInfo; 
	private final Map<IMember<IJavaExpression>, JavaField> fieldInfo;
	
	public JavaCompileState()
	{
		typeInfo = new HashMap<>();
		methodInfo = new HashMap<>();
		fieldInfo = new HashMap<>();
	}
	
	public JavaTypeInfo getTypeInfo(ITypeDefinition<IJavaExpression> type)
	{
		return typeInfo.get(type);
	}
	
	public JavaTypeInfo getTypeInfo(IGenericType<IJavaExpression> type)
	{
		return getTypeInfo(type.getTypeDefinition());
	}
}
