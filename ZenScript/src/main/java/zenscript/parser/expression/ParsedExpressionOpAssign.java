/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.expression;

import stanhebben.zenscript.IZenCompileEnvironment;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.runtime.IAny;
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
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: validate if the prediction rules are sound
		Expression cLeft = left.compile(environment, predictedType);
		Expression cRight = right.compile(environment, cLeft.getType());
		
		Expression value = cLeft.getType().binary(getPosition(), environment, cLeft, cRight, operator);
		
		return left.compilePartial(environment, predictedType).assign(getPosition(), value);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		return null;
	}
}
