/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ICallable<E extends IPartialExpression<E>>
{
	public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments);
	
	public E callWithConstants(CodePosition position, IMethodScope<E> scope, Object... values);
	
	public String getFullName();
	
	public InstancedMethodHeader<E> getMethodHeader();
	
	public E asValue(CodePosition position, IMethodScope<E> scope);
}
