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
public class BoolTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> bool = typeRegistry.bool;
		IGenericType<E> string = typeRegistry.string;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		addUnaryOperator(bool, bool, OperatorType.NOT, compiler::notBool);
		
		addBinaryOperator(bool, bool, bool, OperatorType.AND, compiler::andBool);
		addBinaryOperator(bool, bool, bool, OperatorType.OR, compiler::orBool);
		addBinaryOperator(bool, bool, bool, OperatorType.XOR, compiler::xorBool);
		addBinaryOperator(bool, bool, bool, OperatorType.EQUALS, compiler::equalsBool);
		addBinaryOperator(bool, bool, bool, OperatorType.NOTEQUALS, compiler::notEqualsBool);
		
		addCaster(bool, string, compiler::boolToString);
	}
}
