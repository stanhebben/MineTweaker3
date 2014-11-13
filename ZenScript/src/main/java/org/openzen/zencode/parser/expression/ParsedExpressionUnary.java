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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionUnary extends ParsedExpression {
	private final ParsedExpression value;
	private final OperatorType operator;
	
	public ParsedExpressionUnary(CodePosition position, ParsedExpression value, OperatorType operator) {
		super(position);
		
		this.value = value;
		this.operator = operator;
	}

	@Override
	public IPartialExpression compilePartial(IScopeMethod environment, ZenType predictedType) {
		// TODO: improve type predictions?
		Expression cValue = value.compile(environment, predictedType);
		return cValue.getType().operator(getPosition(), environment, operator, cValue);
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
