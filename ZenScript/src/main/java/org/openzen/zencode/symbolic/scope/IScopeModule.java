/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IScopeModule<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends IScopeGlobal<E, T>
{
	public AccessScope getAccessScope();
}
