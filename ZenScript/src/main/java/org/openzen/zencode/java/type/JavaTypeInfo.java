/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.java.iterator.IJavaIterator;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.member.definition.ConstructorMember;
import org.openzen.zencode.symbolic.type.generic.ConstructorGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 */
public class JavaTypeInfo
{
	private final Type asmType;
	private final IJavaIterator[] iterators;
	
	public JavaTypeInfo(Type asmType, IJavaIterator[] iterators)
	{
		this.asmType = asmType;
		this.iterators = iterators;
	}
	
	public boolean isLarge()
	{
		return asmType.getSize() == 2;
	}
	
	public Type toASMType()
	{
		return asmType;
	}
	
	public String getSignature()
	{
		return asmType.getDescriptor();
	}
	
	public IJavaIterator getIterator(int variables)
	{
		if (variables < iterators.length)
			return iterators[variables];
		else
			return null;
	}
	
	public String getInternalName()
	{
		return asmType.getInternalName();
	}
	
	public String getDescriptor()
	{
		return asmType.getDescriptor();
	}
	
	public boolean isArray()
	{
		
	}
	
	public boolean isList()
	{
		
	}
	
	public JavaTypeInfo getArrayBaseType()
	{
		
	}
	
	public IJavaMethod getConstructor(ConstructorMember<IJavaExpression> constructor)
	{
		
	}
	
	public JavaField getTypeVariableField(ITypeVariable<IJavaExpression> typeVariable)
	{
		
	}
	
	public JavaField getGenericConstructorField(ConstructorGenericParameterBound<IJavaExpression> constructor)
	{
		
	}
	
	public void markGenericConstructorUse(ConstructorGenericParameterBound<IJavaExpression> constructor)
	{
		
	}
}
