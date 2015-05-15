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
public class FloatTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> float_ = typeRegistry.float_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(float_, float_, OperatorType.NEG, compiler::negFloat);
		
		addBinaryOperator(float_, float_, float_, OperatorType.ADD, compiler::addFloat);
		addBinaryOperator(float_, float_, float_, OperatorType.SUB, compiler::subFloat);
		addBinaryOperator(float_, float_, float_, OperatorType.MUL, compiler::mulFloat);
		addBinaryOperator(float_, float_, float_, OperatorType.DIV, compiler::divFloat);
		addBinaryOperator(float_, float_, float_, OperatorType.MOD, compiler::modFloat);
		
		addComparators(bool, float_, compiler::compareFloat);
		
		addCaster(float_, typeRegistry.byte_, compiler::floatToByte);
		addCaster(float_, typeRegistry.ubyte, compiler::floatToUByte);
		addCaster(float_, typeRegistry.short_, compiler::floatToShort);
		addCaster(float_, typeRegistry.ushort, compiler::floatToUShort);
		addCaster(float_, typeRegistry.int_, compiler::floatToInt);
		addCaster(float_, typeRegistry.uint, compiler::floatToUInt);
		addCaster(float_, typeRegistry.long_, compiler::floatToLong);
		addCaster(float_, typeRegistry.ulong, compiler::floatToULong);
		addCaster(float_, typeRegistry.double_, compiler::floatToDouble);
		addCaster(float_, typeRegistry.string, compiler::floatToString);
	}
}
