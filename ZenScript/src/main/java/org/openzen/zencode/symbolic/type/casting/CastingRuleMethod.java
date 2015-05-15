/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.casting;

import java.util.Collections;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CastingRuleMethod<E extends IPartialExpression<E>> implements ICastingRule<E>
{
	private final IVirtualCallable<E> method;
	
	public CastingRuleMethod(IVirtualCallable<E> method)
	{
		this.method = method;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E cast(CodePosition position, IMethodScope<E> scope, E value)
	{
		return method.call(position, scope, value, Collections.<E>emptyList());
	}

	@Override
	public IGenericType<E> getInputType()
	{
		return method.getMethodHeader().getArgumentType(0);
	}

	@Override
	public IGenericType<E> getResultingType()
	{
		return method.getMethodHeader().getReturnType();
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
