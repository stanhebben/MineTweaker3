/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.runtime.AnyString;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedExpressionString extends ParsedExpression
{
	private final String value;

	public ParsedExpressionString(CodePosition position, String value)
	{
		super(position);

		this.value = value;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IScopeMethod<E, T> scope, T asType)
	{
		if (asType == scope.getTypes().getChar()) {
			if (value.length() != 1) {
				scope.error(getPosition(), "Cannot convert string value\"" + value + "\" to char");
				return scope.getExpressionCompiler().invalid(getPosition(), scope, asType);
			} else {
				return scope.getExpressionCompiler().constantChar(getPosition(), scope, value.charAt(0));
			}
		}
		
		return scope.getExpressionCompiler().constantString(getPosition(), scope, value);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return new AnyString(value);
	}
}
