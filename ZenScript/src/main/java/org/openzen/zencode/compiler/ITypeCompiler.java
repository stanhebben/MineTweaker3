/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface ITypeCompiler<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	public T getAny(IModuleScope<E, T> scope);
	
	public T getAnyArray(IModuleScope<E, T> scope);
	
	public T getAnyAnyMap(IModuleScope<E, T> scope);
	
	public T getNull(IModuleScope<E, T> scope);
	
	public T getVoid(IModuleScope<E, T> scope);
	
	public T getBool(IModuleScope<E, T> scope);
	
	public T getByte(IModuleScope<E, T> scope);
	
	public T getUByte(IModuleScope<E, T> scope);
	
	public T getShort(IModuleScope<E, T> scope);
	
	public T getUShort(IModuleScope<E, T> scope);
	
	public T getInt(IModuleScope<E, T> scope);
	
	public T getUInt(IModuleScope<E, T> scope);
	
	public T getLong(IModuleScope<E, T> scope);
	
	public T getULong(IModuleScope<E, T> scope);
	
	public T getFloat(IModuleScope<E, T> scope);
	
	public T getDouble(IModuleScope<E, T> scope);
	
	public T getChar(IModuleScope<E, T> scope);
	
	public T getString(IModuleScope<E, T> scope);
	
	public T getArray(IModuleScope<E, T> scope, T baseType);
	
	public T getMap(IModuleScope<E, T> scope, T keyType, T valueType);
	
	public T getFunction(IModuleScope<E, T> scope, MethodHeader<E, T> header);
	
	public void addExpansion(IModuleScope<E, T> scope, String type, TypeExpansion<E, T> expansion);
	
	public T getStruct(IModuleScope<E, T> scope, SymbolicStruct<E, T> struct);
	
	public T getGeneric(ITypeVariable<E, T> typeVariable);
}
