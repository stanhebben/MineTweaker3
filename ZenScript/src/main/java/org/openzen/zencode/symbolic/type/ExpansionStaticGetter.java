/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ExpansionStaticGetter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		implements IGetter<E, T>
{
	private final PartialStaticMember<E, T> member;
	private final IMethod<E, T> method;
	
	public ExpansionStaticGetter(PartialStaticMember<E, T> member, IMethod<E, T> method)
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
	public IPartialExpression<E, T> compileGet(CodePosition position, IScopeMethod<E, T> scope)
	{
		return method.callStatic(position, scope);
	}
}
