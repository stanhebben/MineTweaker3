/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class RangeTypeDefinition<E extends IPartialExpression<E>> extends BuiltinTypeDefinition<E>
{
	public void init(TypeRegistry<E> typeRegistry, IModuleScope<E> scope)
	{
		IGenericType<E> int_ = scope.getTypeCompiler().int_;
		
		IExpressionCompiler<E> compiler = scope.getExpressionCompiler();
		addGetter("from", int_, compiler::getRangeFrom);
		addGetter("to", int_, compiler::getRangeTo);
	}
}
