/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode;

import java.util.List;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 * The compile environment is supplied by the running instance to provide an
 * interface between the compiler and the environment. The compile environment
 * is only used during compilation, not during execution.
 *
 * @author Stan Hebben
 *
 * @param <E> compiler expression type
 * @param <T> compiler value type
 */
public interface IZenCompileEnvironment<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	/**
	 * Gets the type compiler for the current target language.
	 *
	 * @return type compiler
	 */
	public ITypeCompiler<E, T> getTypeCompiler();

	/**
	 * Gets the expression compiler for the current target language.
	 *
	 * @return expression compiler
	 */
	public IExpressionCompiler<E, T> getExpressionCompiler();

	/**
	 * Gets the error logger used to report warnings and errors.
	 *
	 * @return error logger
	 */
	public ICodeErrorLogger<E, T> getErrorLogger();

	/**
	 * Gets a global symbol. Should return null if the symbol doesn't exist.
	 *
	 * @param position
	 * @param scope
	 * @param name global symbol name
	 * @return symbol
	 */
	public IPartialExpression<E, T> getGlobal(CodePosition position, IMethodScope<E, T> scope, String name);

	/**
	 * Gets a dollar symbol. Should return null if the symbol doesn't exist.
	 *
	 * @param position
	 * @param scope
	 * @param name variable name
	 * @return symbol
	 */
	public IPartialExpression<E, T> getDollar(CodePosition position, IMethodScope<E, T> scope, String name);

	/**
	 * Evaluates the bracketed value. Should return null if it can't be
	 * evaluated. Return a symbol that evaluates to the null value if you intend
	 * to return null.
	 *
	 * @param position
	 * @param scope
	 * @param tokens
	 * @return
	 */
	public IPartialExpression<E, T> getBracketed(CodePosition position, IMethodScope<E, T> scope, List<Token> tokens);

	/**
	 * Evaluates a global value. Should return null if it can't be evaluated
	 * compiled. Return AnyNull.INSTANCE if you intend to return null.
	 *
	 * @param name global variable name
	 * @return evaluated value
	 */
	public IAny evalGlobal(String name);

	/**
	 * Evaluates the dollar value. Should return null if it can't be evaluated
	 * compiled. Return AnyNull.INSTANCE if you intend to return null.
	 *
	 * @param name dollar variable name
	 * @return evaluated value
	 */
	public IAny evalDollar(String name);

	/**
	 * Evaluates the bracketed value. Should return null if it can't be
	 * evaluated and AnyNull.INSTANCE if you intend to return null.
	 *
	 * @param tokens
	 * @return
	 */
	public IAny evalBracketed(List<Token> tokens);
}
