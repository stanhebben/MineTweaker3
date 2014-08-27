/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOpAssign extends ParsedExpression {
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;
	
	public ParsedExpressionOpAssign(ZenPosition position, ParsedExpression left, ParsedExpression right, OperatorType operator) {
		super(position);
		
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	@Override
	public IPartialExpression compile(IScopeMethod environment, ZenType predictedType) {
		// TODO: validate if the prediction rules are sound
		Expression cLeft = left.compile(environment, predictedType).eval();
		Expression cRight = right.compile(environment, cLeft.getType()).eval();
		
		Expression value = cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
		
		return left.compile(environment, predictedType).assign(getPosition(), value);
	}
}
