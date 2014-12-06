/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionCompare extends ParsedExpression
{
	private final ParsedExpression left;
	private final ParsedExpression right;
	private final CompareType type;

	public ParsedExpressionCompare(
			CodePosition position,
			ParsedExpression left,
			ParsedExpression right,
			CompareType type)
	{
		super(position);

		this.left = left;
		this.right = right;
		this.type = type;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IScopeMethod<E, T> scope, T asType)
	{
		E cLeft = left.compile(scope, null);
		E cRight = right.compile(scope, null);
		
		E result = cLeft.getType().compare(getPosition(), scope, cLeft, cRight, type);
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		IAny leftValue = left.eval(environment);
		if (leftValue == null)
			return null;

		IAny rightValue = right.eval(environment);
		if (rightValue == null)
			return null;

		int result = leftValue.compareTo(rightValue);
		switch (type) {
			case LT:
				return AnyBool.valueOf(result < 0);
			case GT:
				return AnyBool.valueOf(result > 0);
			case EQ:
				return AnyBool.valueOf(result == 0);
			case NE:
				return AnyBool.valueOf(result != 0);
			case LE:
				return AnyBool.valueOf(result <= 0);
			case GE:
				return AnyBool.valueOf(result >= 0);
			default:
				throw new AssertionError("Illegal compare type");
		}
	}
}
