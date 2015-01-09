/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Collections;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ExpansionStaticSetter<E extends IPartialExpression<E>> implements ISetter<E>
{
	private final PartialStaticMember<E> member;
	private final IMethod<E> method;
	
	public ExpansionStaticSetter(PartialStaticMember<E> member, IMethod<E> method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public TypeInstance<E> getType()
	{
		return method.getReturnType();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E compile(CodePosition position, IMethodScope<E> scope, E value)
	{
		return method.callStatic(position, scope, Collections.singletonList(value));
	}
}
