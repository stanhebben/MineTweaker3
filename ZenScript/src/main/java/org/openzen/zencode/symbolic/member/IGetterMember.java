/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface IGetterMember<E extends IPartialExpression<E>> extends IMember<E>
{
	public IGenericType<E> getType();
	
	public String getName();
	
	public E getStatic(CodePosition position, IMethodScope<E> scope);
	
	public E getVirtual(CodePosition position, IMethodScope<E> scope, E instance);
}
