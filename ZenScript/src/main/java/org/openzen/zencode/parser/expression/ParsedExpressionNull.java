/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ParsedExpressionNull extends ParsedExpression
{
	public ParsedExpressionNull(CodePosition position)
	{
		super(position);
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IPartialExpression<E> compilePartial(IMethodScope<E> scope, IGenericType<E> predictedType)
	{
		return scope.getExpressionCompiler().constantNull(getPosition(), scope);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?> environment)
	{
		return AnyNull.INSTANCE;
	}
}
