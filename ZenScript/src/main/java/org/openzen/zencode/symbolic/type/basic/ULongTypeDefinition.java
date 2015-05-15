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
public class ULongTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> ulong = typeRegistry.ulong;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(ulong, ulong, OperatorType.INVERT, compiler::invertULong);
		
		addBinaryOperator(ulong, ulong, ulong, OperatorType.ADD, compiler::addULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.SUB, compiler::subULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.MUL, compiler::mulULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.DIV, compiler::divULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.MOD, compiler::modULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.AND, compiler::andULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.OR, compiler::orULong);
		addBinaryOperator(ulong, ulong, ulong, OperatorType.XOR, compiler::xorULong);
		addBinaryOperator(ulong, ulong, int_, OperatorType.SHL, compiler::shlULong);
		addBinaryOperator(ulong, ulong, int_, OperatorType.SHR, compiler::shrULong);
		
		addComparators(bool, ulong, compiler::compareULong);
		
		addCaster(ulong, typeRegistry.byte_, compiler::ulongToByte);
		addCaster(ulong, typeRegistry.ubyte, compiler::ulongToUByte);
		addCaster(ulong, typeRegistry.short_, compiler::ulongToShort);
		addCaster(ulong, typeRegistry.ushort, compiler::ulongToUShort);
		addCaster(ulong, typeRegistry.int_, compiler::ulongToInt);
		addCaster(ulong, typeRegistry.uint, compiler::ulongToUInt);
		addCaster(ulong, typeRegistry.long_, compiler::ulongToLong);
		addCaster(ulong, typeRegistry.float_, compiler::ulongToFloat);
		addCaster(ulong, typeRegistry.double_, compiler::ulongToDouble);
		addCaster(ulong, typeRegistry.string, compiler::ulongToString);
	}
}
