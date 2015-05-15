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
 */
public class StringTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> type = typeRegistry.string;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		addComparators(typeRegistry.bool, type, compiler::compareString);
		
		addBinaryOperator(type, type, typeRegistry.bool, OperatorType.CAT, compiler::catStringBool);
		addBinaryOperator(type, type, typeRegistry.byte_, OperatorType.CAT, compiler::catStringByte);
		addBinaryOperator(type, type, typeRegistry.ubyte, OperatorType.CAT, compiler::catStringUByte);
		addBinaryOperator(type, type, typeRegistry.short_, OperatorType.CAT, compiler::catStringShort);
		addBinaryOperator(type, type, typeRegistry.ushort, OperatorType.CAT, compiler::catStringUShort);
		addBinaryOperator(type, type, typeRegistry.int_, OperatorType.CAT, compiler::catStringInt);
		addBinaryOperator(type, type, typeRegistry.uint, OperatorType.CAT, compiler::catStringUInt);
		addBinaryOperator(type, type, typeRegistry.long_, OperatorType.CAT, compiler::catStringLong);
		addBinaryOperator(type, type, typeRegistry.ulong, OperatorType.CAT, compiler::catStringULong);
		addBinaryOperator(type, type, typeRegistry.float_, OperatorType.CAT, compiler::catStringFloat);
		addBinaryOperator(type, type, typeRegistry.double_, OperatorType.CAT, compiler::catStringDouble);
		addBinaryOperator(type, type, typeRegistry.char_, OperatorType.CAT, compiler::catStringChar);
		addBinaryOperator(type, type, typeRegistry.string, OperatorType.CAT, compiler::catStringString);
		addBinaryOperator(type, typeRegistry.char_, typeRegistry.int_, OperatorType.INDEXGET, compiler::indexString);
		
		addCaster(type, typeRegistry.char_, compiler::stringToChar);
	}
}
