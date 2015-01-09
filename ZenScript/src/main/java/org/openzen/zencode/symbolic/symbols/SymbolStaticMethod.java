/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolStaticMethod<E extends IPartialExpression<E>>
	implements IZenSymbol<E>
{
	private final IMethod<E> method;
	
	public SymbolStaticMethod(IMethod<E> method)
	{
		this.method = method;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialStaticMethod<E>(position, scope, method);
	}
}
