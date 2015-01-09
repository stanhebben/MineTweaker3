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
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionAndAnd extends ParsedExpression
{
	private final ParsedExpression left;
	private final ParsedExpression right;

	public ParsedExpressionAndAnd(CodePosition position, ParsedExpression left, ParsedExpression right)
	{
		super(position);

		this.left = left;
		this.right = right;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T asType)
	{
		return scope.getExpressionCompiler().andAnd(
				getPosition(),
				scope,
				left.compilePartial(scope, asType).eval(),
				right.compilePartial(scope, asType).eval());
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		IAny leftValue = left.eval(environment);
		if (leftValue == null)
			return null;

		if (!leftValue.asBool())
			return AnyBool.FALSE;

		return right.eval(environment);
	}
}
