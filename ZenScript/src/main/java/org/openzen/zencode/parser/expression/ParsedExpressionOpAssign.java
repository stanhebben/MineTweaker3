/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOpAssign extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;
	
	public ParsedExpressionOpAssign(CodePosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
		super(position);
		
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: validate if the prediction rules are sound
		Expression cLeft = left.compile(environment, predictedType);
		Expression cRight = right.compile(environment, cLeft.getType());
		
		Expression value = cLeft.getType().operator(getPosition(), environment, operator, cLeft, cRight);
		
		return left.compilePartial(environment, predictedType).assign(getPosition(), value);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
