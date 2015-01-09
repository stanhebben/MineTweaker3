/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.casting;

import java.util.Collections;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class CastingRuleStaticMethod<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements ICastingRule<E, T>
{
	private final IMethod<E, T> method;
	
	public CastingRuleStaticMethod(IMethod<E, T> method)
	{
		this.method = method;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E cast(CodePosition position, IMethodScope<E, T> scope, E value)
	{
		return method.callStatic(position, scope, Collections.singletonList(value));
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
