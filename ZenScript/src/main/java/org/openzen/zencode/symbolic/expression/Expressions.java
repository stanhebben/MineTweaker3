/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression;

import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class Expressions
{
	private Expressions() {}
	
	public static <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 E[] convert(CodePosition position, IMethodScope<E, T> scope, Object[] constants)
	{
		@SuppressWarnings("unchecked")
		E[] results = (E[]) new IPartialExpression[constants.length];
		
		for (int i = 0; i < constants.length; i++) {
			results[i] = convert(position, scope, constants[i]);
		}
		
		return results;
	}
	
	public static <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		E convert(CodePosition position, IMethodScope<E, T> scope, Object constant)
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
		} else if (constant instanceof IField) {
			@SuppressWarnings("unchecked")
			IField<E, T> field = (IField<E, T>) constant;
			if (!field.isStatic())
				throw new IllegalArgumentException("Can only use static fields as constant values");

			return field.makeStaticGetExpression(position, scope);
		} else {
			throw new IllegalArgumentException("Unsupported constant type: " + constant.getClass());
		}
	}
}
