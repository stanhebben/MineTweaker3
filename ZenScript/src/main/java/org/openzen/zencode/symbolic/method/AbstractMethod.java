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
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public abstract class AbstractMethod<E extends IPartialExpression<E>> implements IMethod<E>
{
	private static <ES extends IPartialExpression<ES>>
		 List<ES> convert(CodePosition position, IMethodScope<ES> scope, Object... arguments)
	{
		@SuppressWarnings("unchecked")
		List<ES> converted = new ArrayList<ES>();
		for (Object argument : arguments) {
			converted.add(scope.getExpressionCompiler().constant(position, scope, argument));
		}
		
		return converted;
	}
	
	@Override
	public E callStaticWithConstants(CodePosition position, IMethodScope<E> scope, Object... constantArguments)
	{
		return callStatic(position, scope, convert(position, scope, constantArguments));
	}
	
	@Override
	public E callVirtualWithConstants(CodePosition position, IMethodScope<E> scope, E target, Object... constantArguments)
	{
		return callVirtual(position, scope, target, convert(position, scope, constantArguments));
	}
	
	@Override
	public MethodHeader<E> getMethodHeader()
	{
		return getFunctionType().getFunctionHeader();
	}
	
	@Override
	public IGenericType<E> getReturnType()
	{
		return getMethodHeader().getReturnType();
	}
}
