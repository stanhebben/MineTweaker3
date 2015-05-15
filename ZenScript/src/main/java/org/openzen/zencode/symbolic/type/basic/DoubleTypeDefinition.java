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
public class DoubleTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> double_ = typeRegistry.float_;
		IGenericType<E> int_ = typeRegistry.int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		
		addUnaryOperator(double_, double_, OperatorType.NEG, compiler::negDouble);
		
		addBinaryOperator(double_, double_, double_, OperatorType.ADD, compiler::addDouble);
		addBinaryOperator(double_, double_, double_, OperatorType.SUB, compiler::subDouble);
		addBinaryOperator(double_, double_, double_, OperatorType.MUL, compiler::mulDouble);
		addBinaryOperator(double_, double_, double_, OperatorType.DIV, compiler::divDouble);
		addBinaryOperator(double_, double_, double_, OperatorType.MOD, compiler::modDouble);
		
		addComparators(bool, double_, compiler::compareDouble);
		
		addCaster(double_, typeRegistry.byte_, compiler::doubleToByte);
		addCaster(double_, typeRegistry.ubyte, compiler::doubleToUByte);
		addCaster(double_, typeRegistry.short_, compiler::doubleToShort);
		addCaster(double_, typeRegistry.ushort, compiler::doubleToUShort);
		addCaster(double_, typeRegistry.int_, compiler::doubleToInt);
		addCaster(double_, typeRegistry.uint, compiler::doubleToUInt);
		addCaster(double_, typeRegistry.long_, compiler::doubleToLong);
		addCaster(double_, typeRegistry.ulong, compiler::doubleToULong);
		addCaster(double_, typeRegistry.float_, compiler::doubleToFloat);
		addCaster(double_, typeRegistry.string, compiler::doubleToString);
	}
}
