/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface ISetterMember<E extends IPartialExpression<E>> extends IMember<E>
{
	public String getName();
		
	public E setStatic(CodePosition position, IMethodScope<E> scope, E value);
	
	public E setVirtual(CodePosition position, IMethodScope<E> scope, E instance, E value);
}
