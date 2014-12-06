/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionInvalid extends ParsedExpression
{
	public ParsedExpressionInvalid(CodePosition position)
	{
		super(position);
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IScopeMethod<E, T> scope, T asType)
	{
		return scope.getExpressionCompiler().invalid(
				getPosition(),
				scope,
				asType == null ? scope.getTypes().getAny() : asType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return null;
	}
}
