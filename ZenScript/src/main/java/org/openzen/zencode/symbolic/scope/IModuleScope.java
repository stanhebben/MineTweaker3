/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IModuleScope<E extends IPartialExpression<E>> extends IGlobalScope<E>
{
	public AccessScope getAccessScope();
	
	public TypeCapture<E> getTypeCapture();
}
