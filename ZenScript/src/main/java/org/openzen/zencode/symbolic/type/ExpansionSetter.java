/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Collections;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.partial.PartialVirtualMember;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ExpansionSetter<E extends IPartialExpression<E>>
		implements ISetter<E>
{
	private final PartialVirtualMember<E> member;
	private final IVirtualCallable<E> method;
	
	public ExpansionSetter(PartialVirtualMember<E> member, IVirtualCallable<E> method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public IGenericType<E> getType()
	{
		return method.getMethodHeader().getArgumentType(1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E compile(CodePosition position, IMethodScope<E> scope, E value)
	{
		return method.call(position, scope, member.getTarget(), Collections.singletonList(value));
	}
}
