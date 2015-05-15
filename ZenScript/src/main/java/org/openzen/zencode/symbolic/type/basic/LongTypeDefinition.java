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
public class LongTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> long_ = typeRegistry.long_;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(long_, long_, OperatorType.NEG, compiler::negLong);
		addUnaryOperator(long_, long_, OperatorType.INVERT, compiler::invertLong);
		
		addBinaryOperator(long_, long_, long_, OperatorType.ADD, compiler::addLong);
		addBinaryOperator(long_, long_, long_, OperatorType.SUB, compiler::subLong);
		addBinaryOperator(long_, long_, long_, OperatorType.MUL, compiler::mulLong);
		addBinaryOperator(long_, long_, long_, OperatorType.DIV, compiler::divLong);
		addBinaryOperator(long_, long_, long_, OperatorType.MOD, compiler::modLong);
		addBinaryOperator(long_, long_, long_, OperatorType.AND, compiler::andLong);
		addBinaryOperator(long_, long_, long_, OperatorType.OR, compiler::orLong);
		addBinaryOperator(long_, long_, long_, OperatorType.XOR, compiler::xorLong);
		addBinaryOperator(long_, long_, int_, OperatorType.SHL, compiler::shlLong);
		addBinaryOperator(long_, long_, int_, OperatorType.SHR, compiler::shrLong);
		
		addComparators(bool, long_, compiler::compareLong);
		
		addCaster(long_, typeRegistry.byte_, compiler::longToByte);
		addCaster(long_, typeRegistry.ubyte, compiler::longToUByte);
		addCaster(long_, typeRegistry.short_, compiler::longToShort);
		addCaster(long_, typeRegistry.ushort, compiler::longToUShort);
		addCaster(long_, typeRegistry.int_, compiler::longToInt);
		addCaster(long_, typeRegistry.uint, compiler::longToUInt);
		addCaster(long_, typeRegistry.ulong, compiler::longToULong);
		addCaster(long_, typeRegistry.float_, compiler::longToFloat);
		addCaster(long_, typeRegistry.double_, compiler::longToDouble);
		addCaster(long_, typeRegistry.string, compiler::longToString);
	}
}
