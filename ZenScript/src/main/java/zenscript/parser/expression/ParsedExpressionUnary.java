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
public class ParsedExpressionUnary extends ParsedExpression {
	private final ParsedExpression value;
	private final OperatorType operator;
	
	public ParsedExpressionUnary(ZenPosition position, ParsedExpression value, OperatorType operator) {
		super(position);
		
		this.value = value;
		this.operator = operator;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve type predictions?
		Expression cValue = value.compile(environment, predictedType);
		return cValue.getType().unary(getPosition(), environment, cValue, operator);
	}

	@Override
	public IAny eval(IZenCompileEnvironment environment) {
		IAny valueValue = value.eval(environment);
		if (valueValue == null)
			return null;
		
		switch (operator) {
			case NOT:
				return valueValue.not();
			case INVERT:
				return valueValue.invert();
			case NEG:
				return valueValue.neg();
			default:
				throw new AssertionError("Invalid operator: " + operator);
		}
	}
}
