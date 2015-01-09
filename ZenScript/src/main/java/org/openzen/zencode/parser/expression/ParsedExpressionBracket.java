/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.parser.expression;

import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public class ParsedExpressionBracket extends ParsedExpression
{
	private final List<Token> tokens;

	public ParsedExpressionBracket(CodePosition position, List<Token> tokens)
	{
		super(position);

		this.tokens = tokens;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 IPartialExpression<E, T> compilePartial(IMethodScope<E, T> scope, T asType)
	{
		IPartialExpression<E, T> result = scope.getEnvironment().getBracketed(getPosition(), scope, tokens);
		if (result == null)
			return errorUnresolved(scope);
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}
		 
	private <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		E errorUnresolved(IMethodScope<E, T> environment)
	{
		environment.getErrorLogger().errorCouldNotResolveBracket(getPosition(), tokens);
		return environment.getExpressionCompiler().invalid(getPosition(), environment);
	}

	@Override
	public IAny eval(IZenCompileEnvironment<?, ?> environment)
	{
		return environment.evalBracketed(tokens);
	}
}
