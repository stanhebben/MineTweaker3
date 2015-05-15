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
public class ShortTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> short_ = typeRegistry.short_;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(short_, short_, OperatorType.NEG, compiler::negShort);
		addUnaryOperator(short_, short_, OperatorType.INVERT, compiler::invertShort);
		
		addBinaryOperator(short_, short_, short_, OperatorType.ADD, compiler::addShort);
		addBinaryOperator(short_, short_, short_, OperatorType.SUB, compiler::subShort);
		addBinaryOperator(short_, short_, short_, OperatorType.MUL, compiler::mulShort);
		addBinaryOperator(short_, short_, short_, OperatorType.DIV, compiler::divShort);
		addBinaryOperator(short_, short_, short_, OperatorType.MOD, compiler::modShort);
		addBinaryOperator(short_, short_, short_, OperatorType.AND, compiler::andShort);
		addBinaryOperator(short_, short_, short_, OperatorType.OR, compiler::orShort);
		addBinaryOperator(short_, short_, short_, OperatorType.XOR, compiler::xorShort);
		addBinaryOperator(short_, short_, int_, OperatorType.SHL, compiler::shlShort);
		addBinaryOperator(short_, short_, int_, OperatorType.SHR, compiler::shrShort);
		
		addComparators(bool, short_, compiler::compareShort);
		
		addCaster(short_, typeRegistry.byte_, compiler::shortToByte);
		addCaster(short_, typeRegistry.ubyte, compiler::shortToUByte);
		addCaster(short_, typeRegistry.ushort, compiler::shortToUShort);
		addCaster(short_, typeRegistry.int_, compiler::shortToInt);
		addCaster(short_, typeRegistry.uint, compiler::shortToUInt);
		addCaster(short_, typeRegistry.long_, compiler::shortToLong);
		addCaster(short_, typeRegistry.ulong, compiler::shortToULong);
		addCaster(short_, typeRegistry.float_, compiler::shortToFloat);
		addCaster(short_, typeRegistry.double_, compiler::shortToDouble);
		addCaster(short_, typeRegistry.string, compiler::shortToString);
	}
}
