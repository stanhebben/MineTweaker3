/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IVirtualCallable<E extends IPartialExpression<E>>
{
	public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments);
	
	public String getFullName();
	
	public InstancedMethodHeader<E> getMethodHeader();
	
	public ICallable<E> bind(E instance);
}
