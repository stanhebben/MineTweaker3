/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class Expressions
{
	private Expressions() {}
	
	public static <E extends IPartialExpression<E>>
		 List<E> convert(CodePosition position, IMethodScope<E> scope, Object[] constants)
	{
		List<E> results = new ArrayList<E>();
		
		for (Object constant : constants) {
			results.add(convert(position, scope, constant));
		}
		
		return results;
	}
	
	public static <E extends IPartialExpression<E>>
		E convert(CodePosition position, IMethodScope<E> scope, Object constant)
	{
		if (constant == null) {
			return scope.getExpressionCompiler().constantNull(position, scope);
		} else if (constant instanceof Byte) {
			return scope.getExpressionCompiler().constantByte(position, scope, (Byte) constant);
		} else if (constant instanceof Short) {
			return scope.getExpressionCompiler().constantShort(position, scope, (Short) constant);
		} else if (constant instanceof Integer) {
			return scope.getExpressionCompiler().constantInt(position, scope, (Integer) constant);
		} else if (constant instanceof Long) {
			return scope.getExpressionCompiler().constantLong(position, scope, (Long) constant);
		} else if (constant instanceof Float) {
			return scope.getExpressionCompiler().constantFloat(position, scope, (Float) constant);
		} else if (constant instanceof Double) {
			return scope.getExpressionCompiler().constantDouble(position, scope, (Double) constant);
		} else if (constant instanceof Character) {
			return scope.getExpressionCompiler().constantChar(position, scope, (Character) constant);
		} else if (constant instanceof String) {
			return scope.getExpressionCompiler().constantString(position, scope, (String) constant);
		} else if (constant instanceof Boolean) {
			return scope.getExpressionCompiler().constantBool(position, scope, (Boolean) constant);
		} else {
			throw new IllegalArgumentException("Unsupported constant type: " + constant.getClass());
		}
	}
}
