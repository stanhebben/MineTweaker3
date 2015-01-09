/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionUnary extends ParsedExpression
{
	private final ParsedExpression value;
	private final OperatorType operator;

	public ParsedExpressionUnary(CodePosition position, ParsedExpression value, OperatorType operator)
	{
		super(position);

		this.value = value;
		this.operator = operator;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> environment, TypeInstance<E> predictedType)
	{
		E cValue = value.compile(environment, predictedType);
		return cValue.getType().unary(getPosition(), environment, operator, cValue);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
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
