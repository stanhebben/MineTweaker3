/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
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
		 List<ES> convert(CodePosition position, IMethodScope<ES, TS> scope, Object... arguments)
	{
		@SuppressWarnings("unchecked")
		List<ES> converted = new ArrayList<ES>();
		for (Object argument : arguments) {
			converted.add(scope.getExpressionCompiler().constant(position, scope, argument));
		}
		
		return converted;
	}
	
	@Override
	public E callStaticWithConstants(CodePosition position, IMethodScope<E, T> scope, Object... constantArguments)
	{
		return callStatic(position, scope, convert(position, scope, constantArguments));
	}
	
	@Override
	public E callVirtualWithConstants(CodePosition position, IMethodScope<E, T> scope, E target, Object... constantArguments)
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
