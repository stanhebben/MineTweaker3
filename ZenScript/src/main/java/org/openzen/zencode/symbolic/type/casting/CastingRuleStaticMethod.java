/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.casting;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class CastingRuleStaticMethod<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements ICastingRule<E, T>
{
	private final IMethod<E, T> method;
	
	public CastingRuleStaticMethod(IMethod<E, T> method)
	{
		this.method = method;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E cast(CodePosition position, IScopeMethod<E, T> scope, E value)
	{
		return method.callStatic(position, scope, value);
	}

	@Override
	public T getInputType()
	{
		return method.getMethodHeader().getArgumentType(0);
	}

	@Override
	public T getResultingType()
	{
		return method.getReturnType();
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
