/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.compiler;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ITypeCompiler<E extends IPartialExpression<E>>
{
	public TypeInstance<E> getAny(IModuleScope<E> scope);
	
	public TypeInstance<E> getAnyArray(IModuleScope<E> scope);
	
	public TypeInstance<E> getAnyAnyMap(IModuleScope<E> scope);
	
	public TypeInstance<E> getNull(IModuleScope<E> scope);
	
	public TypeInstance<E> getVoid(IModuleScope<E> scope);
	
	public TypeInstance<E> getBool(IModuleScope<E> scope);
	
	public TypeInstance<E> getByte(IModuleScope<E> scope);
	
	public TypeInstance<E> getUByte(IModuleScope<E> scope);
	
	public TypeInstance<E> getShort(IModuleScope<E> scope);
	
	public TypeInstance<E> getUShort(IModuleScope<E> scope);
	
	public TypeInstance<E> getInt(IModuleScope<E> scope);
	
	public TypeInstance<E> getUInt(IModuleScope<E> scope);
	
	public TypeInstance<E> getLong(IModuleScope<E> scope);
	
	public TypeInstance<E> getULong(IModuleScope<E> scope);
	
	public TypeInstance<E> getFloat(IModuleScope<E> scope);
	
	public TypeInstance<E> getDouble(IModuleScope<E> scope);
	
	public TypeInstance<E> getChar(IModuleScope<E> scope);
	
	public TypeInstance<E> getString(IModuleScope<E> scope);
	
	public TypeInstance<E> getArray(IModuleScope<E> scope, TypeInstance<E> baseType);
	
	public TypeInstance<E> getMap(IModuleScope<E> scope, TypeInstance<E> keyType, TypeInstance<E> valueType);
	
	public TypeInstance<E> getFunction(IModuleScope<E> scope, MethodHeader<E> header);
	
	public void addExpansion(IModuleScope<E> scope, String type, TypeExpansion<E> expansion);
	
	public TypeInstance<E> getStruct(IModuleScope<E> scope, SymbolicStruct<E> struct);
	
	public TypeInstance<E> getGeneric(ITypeVariable<E> typeVariable);
}
