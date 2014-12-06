/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolStaticMethod<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IZenSymbol<E, T>
{
	private final IMethod<E, T> method;
	
	public SymbolStaticMethod(IMethod<E, T> method)
	{
		this.method = method;
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IScopeMethod<E, T> scope)
	{
		return new PartialStaticMethod<E, T>(position, scope, method);
	}
}
