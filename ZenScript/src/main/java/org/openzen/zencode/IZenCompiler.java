/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode;

import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IZenCompiler<E extends IPartialExpression<E>>
{
	/**
	 * Gets the expression compiler for the current target language.
	 *
	 * @return expression compiler
	 */
	public IExpressionCompiler<E> getExpressionCompiler();
}
