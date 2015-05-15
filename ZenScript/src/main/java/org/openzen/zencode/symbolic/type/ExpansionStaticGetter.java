/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Collections;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ExpansionStaticGetter<E extends IPartialExpression<E>>
		implements IGetter<E>
{
	private final PartialStaticMember<E> member;
	private final ICallable<E> method;
	
	public ExpansionStaticGetter(PartialStaticMember<E> member, ICallable<E> method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public IGenericType<E> getType()
	{
		return method.getMethodHeader().getReturnType();
	}

	@Override
	@SuppressWarnings("unchecked")
	public IPartialExpression<E> compileGet(CodePosition position, IMethodScope<E> scope)
	{
		return method.call(position, scope, Collections.<E>emptyList());
	}
}
