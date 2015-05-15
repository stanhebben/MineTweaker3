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
public class UByteTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> ubyte = typeRegistry.ubyte;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(ubyte, ubyte, OperatorType.INVERT, compiler::invertByte);
		
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.ADD, compiler::addUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.SUB, compiler::subUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.MUL, compiler::mulUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.DIV, compiler::divUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.MOD, compiler::modUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.AND, compiler::andUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.OR, compiler::orUByte);
		addBinaryOperator(ubyte, ubyte, ubyte, OperatorType.XOR, compiler::xorUByte);
		addBinaryOperator(ubyte, ubyte, int_, OperatorType.SHL, compiler::shlUByte);
		addBinaryOperator(ubyte, ubyte, int_, OperatorType.SHR, compiler::shrUByte);
		
		addComparators(bool, ubyte, compiler::compareUByte);
		
		addCaster(ubyte, typeRegistry.byte_, compiler::ubyteToByte);
		addCaster(ubyte, typeRegistry.short_, compiler::ubyteToShort);
		addCaster(ubyte, typeRegistry.ushort, compiler::ubyteToUShort);
		addCaster(ubyte, typeRegistry.int_, compiler::ubyteToInt);
		addCaster(ubyte, typeRegistry.uint, compiler::ubyteToUInt);
		addCaster(ubyte, typeRegistry.long_, compiler::ubyteToLong);
		addCaster(ubyte, typeRegistry.ulong, compiler::ubyteToULong);
		addCaster(ubyte, typeRegistry.float_, compiler::ubyteToFloat);
		addCaster(ubyte, typeRegistry.double_, compiler::ubyteToDouble);
		addCaster(ubyte, typeRegistry.string, compiler::ubyteToString);
	}
}
