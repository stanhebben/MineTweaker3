/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionOrOr;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOrOr extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	
	public ParsedExpressionOrOr(CodePosition position, ParsedExpression left, ParsedExpression right) {
		super(position);
		
		this.left = left;
		this.right = right;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod scope, ZenType predictedType) {
		Expression cLeft = left.compile(scope, predictedType);
		Expression cRight = right.compile(scope, predictedType);
		
		ZenType type;
		if (cRight.getType().canCastImplicit(scope.getAccessScope(), cLeft.getType())) {
			type = cLeft.getType();
		} else if (cLeft.getType().canCastImplicit(scope.getAccessScope(), cRight.getType())) {
			type = cRight.getType();
		} else {
			scope.error(getPosition(), "These types could not be unified: " + cLeft.getType() + " and " + cRight.getType());
			type = scope.getTypes().ANY;
		}
		
		return new ExpressionOrOr(
				getPosition(),
				scope,
				cLeft.cast(getPosition(), type),
				cRight.cast(getPosition(), type));
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny leftValue = left.eval(environment);
		if (leftValue == null)
			return null;
		
		if (leftValue != AnyBool.FALSE && leftValue != AnyNull.INSTANCE)
			return leftValue;
		
		IAny rightValue = right.eval(environment);
		if (rightValue == null)
			return null;
		
		return rightValue;
	}
}
