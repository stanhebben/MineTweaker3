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
public class ByteTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> byte_ = typeRegistry.byte_;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(byte_, byte_, OperatorType.NEG, compiler::negByte);
		addUnaryOperator(byte_, byte_, OperatorType.INVERT, compiler::invertByte);
		
		addBinaryOperator(byte_, byte_, byte_, OperatorType.ADD, compiler::addByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.SUB, compiler::subByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.MUL, compiler::mulByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.DIV, compiler::divByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.MOD, compiler::modByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.AND, compiler::andByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.OR, compiler::orByte);
		addBinaryOperator(byte_, byte_, byte_, OperatorType.XOR, compiler::xorByte);
		addBinaryOperator(byte_, byte_, int_, OperatorType.SHL, compiler::shlByte);
		addBinaryOperator(byte_, byte_, int_, OperatorType.SHR, compiler::shrByte);
		
		addComparators(bool, byte_, compiler::compareByte);
		
		addCaster(byte_, typeRegistry.ubyte, compiler::byteToUByte);
		addCaster(byte_, typeRegistry.short_, compiler::byteToShort);
		addCaster(byte_, typeRegistry.ushort, compiler::byteToUShort);
		addCaster(byte_, typeRegistry.int_, compiler::byteToInt);
		addCaster(byte_, typeRegistry.uint, compiler::byteToUInt);
		addCaster(byte_, typeRegistry.long_, compiler::byteToLong);
		addCaster(byte_, typeRegistry.ulong, compiler::byteToULong);
		addCaster(byte_, typeRegistry.float_, compiler::byteToFloat);
		addCaster(byte_, typeRegistry.double_, compiler::byteToDouble);
		addCaster(byte_, typeRegistry.string, compiler::byteToString);
	}
}
