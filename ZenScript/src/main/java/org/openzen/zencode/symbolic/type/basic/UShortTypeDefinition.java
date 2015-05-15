/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import org.openzen.zencode.annotations.OperatorType;
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
public class UShortTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> ushort = typeRegistry.ushort;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(ushort, ushort, OperatorType.INVERT, compiler::invertUShort);
		
		addBinaryOperator(ushort, ushort, ushort, OperatorType.ADD, compiler::addUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.SUB, compiler::subUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.MUL, compiler::mulUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.DIV, compiler::divUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.MOD, compiler::modUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.AND, compiler::andUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.OR, compiler::orUShort);
		addBinaryOperator(ushort, ushort, ushort, OperatorType.XOR, compiler::xorUShort);
		addBinaryOperator(ushort, ushort, int_, OperatorType.SHL, compiler::shlUShort);
		addBinaryOperator(ushort, ushort, int_, OperatorType.SHR, compiler::shrUShort);
		
		addComparators(bool, ushort, compiler::compareUShort);
		
		addCaster(ushort, typeRegistry.byte_, compiler::ushortToByte);
		addCaster(ushort, typeRegistry.ubyte, compiler::ushortToUByte);
		addCaster(ushort, typeRegistry.short_, compiler::ushortToShort);
		addCaster(ushort, typeRegistry.int_, compiler::ushortToInt);
		addCaster(ushort, typeRegistry.uint, compiler::ushortToUInt);
		addCaster(ushort, typeRegistry.long_, compiler::ushortToLong);
		addCaster(ushort, typeRegistry.ulong, compiler::ushortToULong);
		addCaster(ushort, typeRegistry.float_, compiler::ushortToFloat);
		addCaster(ushort, typeRegistry.double_, compiler::ushortToDouble);
		addCaster(ushort, typeRegistry.string, compiler::ushortToString);
	}
}
