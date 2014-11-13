/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression;

import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionBool;
import stanhebben.zenscript.expression.ExpressionFloat;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionString;

/**
 *
 * @author Stan
 */
public class Expressions
{
	private Expressions() {}
	
	public static Expression[] convert(CodePosition position, IScopeMethod scope, Object[] constants)
	{
		Expression[] results = new Expression[constants.length];
		for (int i = 0; i < constants.length; i++) {
			if (constants[i] == null) {
				results[i] = new ExpressionNull(position, scope);
			} else if (constants[i] instanceof Byte) {
				results[i] = new ExpressionInt(position, scope, (Byte) constants[i], scope.getTypes().BYTE);
			} else if (constants[i] instanceof Short) {
				results[i] = new ExpressionInt(position, scope, (Short) constants[i], scope.getTypes().SHORT);
			} else if (constants[i] instanceof Integer) {
				results[i] = new ExpressionInt(position, scope, (Integer) constants[i], scope.getTypes().INT);
			} else if (constants[i] instanceof Long) {
				results[i] = new ExpressionInt(position, scope, (Long) constants[i], scope.getTypes().LONG);
			} else if (constants[i] instanceof Float) {
				results[i] = new ExpressionFloat(position, scope, (Float) constants[i], scope.getTypes().FLOAT);
			} else if (constants[i] instanceof Double) {
				results[i] = new ExpressionFloat(position, scope, (Double) constants[i], scope.getTypes().DOUBLE);
			} else if (constants[i] instanceof String) {
				results[i] = new ExpressionString(position, scope, (String) constants[i]);
			} else if (constants[i] instanceof Boolean) {
				results[i] = new ExpressionBool(position, scope, (Boolean) constants[i]);
			} else if (constants[i] instanceof IField) {
				IField field = (IField) constants[i];
				if (!field.isStatic())
					throw new IllegalArgumentException("Can only use static fields as constant values");
				
				results[i] = field.makeStaticGetExpression(position, scope);
			} else {
				throw new IllegalArgumentException("Unsupported constant type: " + constants[i].getClass());
			}
		}
		
		return results;
	}
}
