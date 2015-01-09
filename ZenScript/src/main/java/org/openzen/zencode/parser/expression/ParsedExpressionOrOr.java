/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionOrOr extends ParsedExpression
{
	private final ParsedExpression left;
	private final ParsedExpression right;

	public ParsedExpressionOrOr(CodePosition position, ParsedExpression left, ParsedExpression right)
	{
		super(position);

		this.left = left;
		this.right = right;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, TypeInstance<E> predictedType)
	{
		E cLeft = left.compile(scope, predictedType);
		E cRight = right.compile(scope, predictedType);

		TypeInstance<E> type;
		if (cRight.getType().canCastImplicit(cLeft.getType()))
			type = cLeft.getType();
		else if (cLeft.getType().canCastImplicit(cRight.getType()))
			type = cRight.getType();
		else {
			scope.getErrorLogger().errorCannotCombineTypes(getPosition(), cLeft.getType(), cRight.getType());
			type = scope.getTypeCompiler().getAny(scope);
		}

		return scope.getExpressionCompiler().orOr(
				getPosition(),
				scope,
				cLeft.cast(getPosition(), type),
				cRight.cast(getPosition(), type));
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
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
