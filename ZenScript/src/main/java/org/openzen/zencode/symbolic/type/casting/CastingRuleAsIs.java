/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.casting;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CastingRuleAsIs<E extends IPartialExpression<E>> implements ICastingRule<E>
{
	private final IGenericType<E> from;
	private final IGenericType<E> to;
	
	public CastingRuleAsIs(IGenericType<E> from, IGenericType<E> to)
	{
		this.from = from;
		this.to = to;
	}
	
	@Override
	public E cast(CodePosition position, IMethodScope<E> scope, E value)
	{
		return value;
	}

	@Override
	public IGenericType<E> getInputType()
	{
		return from;
	}

	@Override
	public IGenericType<E> getResultingType()
	{
		return to;
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
