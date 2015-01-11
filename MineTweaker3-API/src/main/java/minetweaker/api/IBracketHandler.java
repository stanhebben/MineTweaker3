/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api;

import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 * Bracket handlers enable processing of the bracket syntax.
 *
 * Inside brackets, any kind of token is acceptable except the closing bracket.
 * (&gt;) Bracket handlers (multiple handlers can be registered) will resolve
 * these tokens into actual values. Values have to be ZenScript symbols and will
 * resolve at compile-time.
 *
 * These may of course return an expression that are executed on run-time, but
 * the type of the resulting value must be known compile-time.
 *
 * @author Stan Hebben
 */
public interface IBracketHandler
{
	/**
	 * Resolves a set of tokens.
	 *
	 * If the series of tokens is unrecognized, this method should return null.
	 *
	 * @param position
	 * @param scope
	 * @param tokens token stream to be detected
	 * @return the resolved symbol, or null
	 */
	public IJavaExpression resolve(CodePosition position, IMethodScope<IJavaExpression> scope, List<Token> tokens);

	/**
	 * Evaluates the compile-time value of the given bracket value.
	 *
	 * If the series of tokens is unrecognized, or if this value cannot be
	 * evaluated at compile-time, this method should return null.
	 *
	 * @param tokens token stream to be detected
	 * @return the resolved compile-time value, or null
	 */
	public IAny eval(List<Token> tokens);
}
