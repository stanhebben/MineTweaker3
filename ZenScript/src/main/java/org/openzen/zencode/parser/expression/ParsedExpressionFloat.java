/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyDouble;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionFloat extends ParsedExpression
{
	private final double value;

	public ParsedExpressionFloat(CodePosition position, double value)
	{
		super(position);

		this.value = value;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T asType)
	{
		if (asType == scope.getTypeCompiler().getFloat(scope))
			return scope.getExpressionCompiler().constantFloat(getPosition(), scope, (float) value);
		else if (asType == scope.getTypeCompiler().getDouble(scope) || asType == null)
			return scope.getExpressionCompiler().constantDouble(getPosition(), scope, value);
		else
			return scope.getExpressionCompiler()
					.constantDouble(getPosition(), scope, value)
					.cast(getPosition(), asType);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return new AnyDouble(value);
	}
}
