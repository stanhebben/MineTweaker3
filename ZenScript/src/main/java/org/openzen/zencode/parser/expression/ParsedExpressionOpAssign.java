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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOpAssign extends ParsedExpression
{
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final OperatorType operator;

	public ParsedExpressionOpAssign(CodePosition position, ParsedExpression left, ParsedExpression right, OperatorType operator)
	{
		super(position);

		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> environment, T predictedType)
	{
		E cLeft = left.compile(environment, predictedType);
		E cRight = right.compile(environment, cLeft.getType().predictOperatorArgumentType(operator).get(0));
		
		E value = cLeft.getType().binary(getPosition(), environment, operator, cLeft, cRight);
		return value;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return null;
	}
}
