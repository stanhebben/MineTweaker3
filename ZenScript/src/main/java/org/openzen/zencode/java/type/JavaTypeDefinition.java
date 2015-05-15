/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.annotations.ZenStruct;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.type.TypeDefinition;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 */
public class JavaTypeDefinition extends TypeDefinition<IJavaExpression>
{
	public JavaTypeDefinition(Class<?> cls)
	{
		super(getTypeParameters(cls), isStruct(cls), cls.isInterface());
		
		// TODO: add members
		// TODO: add map and list interfaces, if necessary
	}
	
	@SuppressWarnings("unchecked")
	private static List<? extends ITypeVariable<IJavaExpression>> getTypeParameters(Class<?> cls)
	{
		TypeVariable[] typeVariables = cls.getTypeParameters();
		List<JavaTypeVariable> convertedTypeVariables = new ArrayList<JavaTypeVariable>();
		for (TypeVariable<Class<?>> typeVariable : typeVariables) {
			convertedTypeVariables.add(new JavaTypeVariable(typeVariable));
		}
		return convertedTypeVariables;
	}
	
	private static boolean isStruct(Class<?> cls)
	{
		return cls.getAnnotation(ZenStruct.class) != null;
	}
	
	private static class JavaTypeVariable implements ITypeVariable<IJavaExpression>
	{
		private final List<IGenericParameterBound<IJavaExpression>> bounds;
		
		public JavaTypeVariable(TypeVariable<Class<?>> typeVariable)
		{
			bounds = new ArrayList<IGenericParameterBound<IJavaExpression>>();
			for (Type t : typeVariable.getBounds()) {
				// TODO: add bounds
			}
		}

		@Override
		public List<IGenericParameterBound<IJavaExpression>> getBounds()
		{
			return bounds;
		}
	}
}
