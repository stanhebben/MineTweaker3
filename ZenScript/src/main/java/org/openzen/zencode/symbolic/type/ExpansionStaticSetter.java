/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ExpansionStaticSetter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		implements ISetter<E, T>
{
	private final PartialStaticMember<E, T> member;
	private final IMethod<E, T> method;
	
	public ExpansionStaticSetter(PartialStaticMember<E, T> member, IMethod<E, T> method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public T getType()
	{
		return method.getReturnType();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E compile(CodePosition position, IScopeMethod<E, T> scope, E value)
	{
		return method.callStatic(position, scope, value);
	}
}
