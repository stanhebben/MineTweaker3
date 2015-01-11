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
	private Type asmType;
	
	public JavaTypeInfo(Type asmType)
	{
		this.asmType = asmType;
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
		
	}
	
	public IJavaIterator getIterator(int variables)
	{
		
	}
}
