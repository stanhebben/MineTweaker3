/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.AnyLong;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionInt extends ParsedExpression {
	private final long value;
	
	public ParsedExpressionInt(CodePosition position, long value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		Expression result = new ExpressionInt(getPosition(), environment, value, environment.getTypes().INT);
		if (predictedType != null) {
			result = result.cast(getPosition(), predictedType);
		}
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return new AnyLong(value);
	}
}
