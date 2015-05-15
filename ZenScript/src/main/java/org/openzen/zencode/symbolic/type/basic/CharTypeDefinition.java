/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CharTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> type = typeRegistry.char_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		addComparators(typeRegistry.bool, type, compiler::compareChar);
		
		addCaster(type, typeRegistry.byte_, compiler::charToByte);
		addCaster(type, typeRegistry.ubyte, compiler::charToUByte);
		addCaster(type, typeRegistry.short_, compiler::charToShort);
		addCaster(type, typeRegistry.ushort, compiler::charToUShort);
		addCaster(type, typeRegistry.int_, compiler::charToInt);
		addCaster(type, typeRegistry.uint, compiler::charToUInt);
		addCaster(type, typeRegistry.string, compiler::charToString);
	}
}
