/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.ExpressionInvalid;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionInvalid extends ParsedExpression {
	public ParsedExpressionInvalid(CodePosition position) {
		super(position);
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		return new ExpressionInvalid(getPosition(), environment, predictedType == null ? environment.getTypes().ANY : predictedType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
