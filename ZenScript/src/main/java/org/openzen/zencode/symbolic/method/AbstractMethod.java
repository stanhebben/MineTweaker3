/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public abstract class AbstractMethod<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IMethod<E, T>
{
	private static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 ES[] convert(CodePosition position, IScopeMethod<ES, TS> scope, Object... arguments)
	{
		@SuppressWarnings("unchecked")
		ES[] converted = (ES[]) new IPartialExpression[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			converted[i] = scope.getExpressionCompiler().constant(position, scope, arguments[i]);
		}
		
		return converted;
	}
	
	@Override
	public E callStaticWithConstants(CodePosition position, IScopeMethod<E, T> scope, Object... constantArguments)
	{
		return callStatic(position, scope, convert(position, scope, constantArguments));
	}
	
	@Override
	public E callVirtualWithConstants(CodePosition position, IScopeMethod<E, T> scope, E target, Object... constantArguments)
	{
		return callVirtual(position, scope, target, convert(position, scope, constantArguments));
	}
	
	@Override
	public MethodHeader<E, T> getMethodHeader()
	{
		return getFunctionType().getFunctionHeader();
	}
	
	@Override
	public T getReturnType()
	{
		return getMethodHeader().getReturnType();
	}
}
