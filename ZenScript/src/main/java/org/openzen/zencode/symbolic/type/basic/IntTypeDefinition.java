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
public class IntTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(int_, int_, OperatorType.NEG, compiler::negInt);
		addUnaryOperator(int_, int_, OperatorType.INVERT, compiler::invertInt);
		
		addBinaryOperator(int_, int_, int_, OperatorType.ADD, compiler::addInt);
		addBinaryOperator(int_, int_, int_, OperatorType.SUB, compiler::subInt);
		addBinaryOperator(int_, int_, int_, OperatorType.MUL, compiler::mulInt);
		addBinaryOperator(int_, int_, int_, OperatorType.DIV, compiler::divInt);
		addBinaryOperator(int_, int_, int_, OperatorType.MOD, compiler::modInt);
		addBinaryOperator(int_, int_, int_, OperatorType.AND, compiler::andInt);
		addBinaryOperator(int_, int_, int_, OperatorType.OR, compiler::orInt);
		addBinaryOperator(int_, int_, int_, OperatorType.XOR, compiler::xorInt);
		addBinaryOperator(int_, int_, int_, OperatorType.SHL, compiler::shlInt);
		addBinaryOperator(int_, int_, int_, OperatorType.SHR, compiler::shrInt);
		
		addComparators(bool, int_, compiler::compareInt);
		
		addCaster(int_, typeRegistry.byte_, compiler::intToByte);
		addCaster(int_, typeRegistry.ubyte, compiler::intToUByte);
		addCaster(int_, typeRegistry.short_, compiler::intToShort);
		addCaster(int_, typeRegistry.ushort, compiler::intToUShort);
		addCaster(int_, typeRegistry.uint, compiler::intToUInt);
		addCaster(int_, typeRegistry.long_, compiler::intToLong);
		addCaster(int_, typeRegistry.ulong, compiler::intToULong);
		addCaster(int_, typeRegistry.float_, compiler::intToFloat);
		addCaster(int_, typeRegistry.double_, compiler::intToDouble);
		addCaster(int_, typeRegistry.string, compiler::intToString);
	}
}
