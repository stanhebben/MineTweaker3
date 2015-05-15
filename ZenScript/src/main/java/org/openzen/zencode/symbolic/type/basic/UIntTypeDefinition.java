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
public class UIntTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> uint = typeRegistry.uint;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(uint, uint, OperatorType.INVERT, compiler::invertUInt);
		
		addBinaryOperator(uint, uint, uint, OperatorType.ADD, compiler::addUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.SUB, compiler::subUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.MUL, compiler::mulUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.DIV, compiler::divUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.MOD, compiler::modUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.AND, compiler::andUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.OR, compiler::orUInt);
		addBinaryOperator(uint, uint, uint, OperatorType.XOR, compiler::xorUInt);
		addBinaryOperator(uint, uint, int_, OperatorType.SHL, compiler::shlUInt);
		addBinaryOperator(uint, uint, int_, OperatorType.SHR, compiler::shrUInt);
		
		addComparators(bool, uint, compiler::compareUInt);
		
		addCaster(uint, typeRegistry.byte_, compiler::uintToByte);
		addCaster(uint, typeRegistry.ubyte, compiler::uintToUByte);
		addCaster(uint, typeRegistry.short_, compiler::ushortToShort);
		addCaster(uint, typeRegistry.ushort, compiler::uintToUShort);
		addCaster(uint, typeRegistry.int_, compiler::uintToInt);
		addCaster(uint, typeRegistry.long_, compiler::uintToLong);
		addCaster(uint, typeRegistry.ulong, compiler::uintToULong);
		addCaster(uint, typeRegistry.float_, compiler::uintToFloat);
		addCaster(uint, typeRegistry.double_, compiler::uintToDouble);
		addCaster(uint, typeRegistry.string, compiler::uintToString);
	}
}
