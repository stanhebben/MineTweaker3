/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class BoundCallable<E extends IPartialExpression<E>> implements ICallable<E>
{
	private final IVirtualCallable<E> callable;
	private final E instance;
	
	public BoundCallable(IVirtualCallable<E> callable, E instance)
	{
		this.callable = callable;
		this.instance = instance;
	}
	
	@Override
	public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments)
	{
		return callable.call(position, scope, instance, arguments);
	}

	@Override
	public E callWithConstants(CodePosition position, IMethodScope<E> scope, Object... values)
	{
		return callable.call(position, scope, instance, Expressions.convert(position, scope, values));
	}

	@Override
	public String getFullName()
	{
		return callable.getFullName();
	}

	@Override
	public InstancedMethodHeader<E> getMethodHeader()
	{
		return callable.getMethodHeader();
	}

	@Override
	public E asValue(CodePosition position, IMethodScope<E> scope)
	{
		// TODO: how?
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
