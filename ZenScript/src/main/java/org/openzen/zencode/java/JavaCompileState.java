/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.JavaTypeInfo;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 */
public class JavaCompileState
{
	private final Map<ITypeDefinition<IJavaExpression>, JavaTypeInfo> typeInfo;
	
	public JavaCompileState()
	{
		typeInfo = new HashMap<>();
	}
	
	public JavaTypeInfo getTypeInfo(IGenericType<IJavaExpression> type)
	{
		
	}
	
	public JavaTypeInfo getStaticTypeInfo(ITypeVariable<IJavaExpression> typeVariable)
	{
		
	}
}
