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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CastingRuleStaticMethod<E extends IPartialExpression<E>> implements ICastingRule<E>
{
	private final IMethod<E> method;
	
	public CastingRuleStaticMethod(IMethod<E> method)
	{
		this.method = method;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E cast(CodePosition position, IMethodScope<E> scope, E value)
	{
		return method.callStatic(position, scope, Collections.singletonList(value));
	}

	@Override
	public TypeInstance<E> getInputType()
	{
		return method.getMethodHeader().getArgumentType(0);
	}

	@Override
	public TypeInstance<E> getResultingType()
	{
		return method.getReturnType();
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
