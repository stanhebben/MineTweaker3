/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionFloat;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.AnyDouble;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionFloat extends ParsedExpression {
	private final double value;
	
	public ParsedExpressionFloat(CodePosition position, double value) {
		super(position);
		
		this.value = value;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionFloat(
				getPosition(),
				environment,
				value,
				predictedType == environment.getTypes().FLOAT ? predictedType : environment.getTypes().DOUBLE);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return new AnyDouble(value);
	}
}
