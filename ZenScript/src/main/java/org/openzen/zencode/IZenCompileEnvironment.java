/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode;

import java.util.List;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;

/**
 * The compile environment is supplied by the running instance to provide an
 * interface between the compiler and the environment. The compile environment
 * is only used during compilation, not during execution.
 *
 * @author Stan Hebben
 *
 * @param <E> compiler expression type
 */
public interface IZenCompileEnvironment<E extends IPartialExpression<E>>
{
	/**
	 * Gets the error logger used to report warnings and errors.
	 *
	 * @return error logger
	 */
	public ICodeErrorLogger<E> getErrorLogger();

	/**
	 * Gets a global symbol. Should return null if the symbol doesn't exist.
	 *
	 * @param name global symbol name
	 * @return symbol
	 */
	public IZenSymbol<E> getGlobal(String name);

	/**
	 * Gets a dollar symbol. Should return null if the symbol doesn't exist.
	 *
	 * @param position
	 * @param scope
	 * @param name variable name
	 * @return symbol
	 */
	public IPartialExpression<E> getDollar(CodePosition position, IMethodScope<E> scope, String name);

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
	public IPartialExpression<E> getBracketed(CodePosition position, IMethodScope<E> scope, List<Token> tokens);
	
	/**
	 * Retrieves the root package. Importable definitions must be stored in this
	 * package (and its subpackages).
	 * 
	 * @return root package
	 */
	public ZenPackage<E> getRootPackage();
	
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
