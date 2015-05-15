/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.java.iterator.IJavaIterator;

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
}
