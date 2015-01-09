/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.packages;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IPackageSymbol<E extends IPartialExpression<E>>
{
	public IZenSymbol<E> toSymbol();
}
