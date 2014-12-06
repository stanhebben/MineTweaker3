/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.type.TypeExpansion;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface ITypeCompiler<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public T getAny();
	
	public T getAnyArray();
	
	public T getAnyAnyMap();
	
	public T getNull();
	
	public T getVoid();
	
	public T getBool();
	
	public T getByte();
	
	public T getUByte();
	
	public T getShort();
	
	public T getUShort();
	
	public T getInt();
	
	public T getUInt();
	
	public T getLong();
	
	public T getULong();
	
	public T getFloat();
	
	public T getDouble();
	
	public T getChar();
	
	public T getString();
	
	public T getArray(T baseType);
	
	public T getMap(T keyType, T valueType);
	
	public T getFunction(MethodHeader<E, T> header);
	
	public void addExpansion(String type, TypeExpansion<E, T> expansion);
}
