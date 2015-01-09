/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IModuleScope<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends IGlobalScope<E, T>
{
	public AccessScope getAccessScope();
	
	public TypeCapture<E, T> getTypeCapture();
}
