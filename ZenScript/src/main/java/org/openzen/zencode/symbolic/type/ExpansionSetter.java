/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Arrays;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.partial.PartialVirtualMember;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ExpansionSetter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		implements ISetter<E, T>
{
	private final PartialVirtualMember<E, T> member;
	private final IMethod<E, T> method;
	
	public ExpansionSetter(PartialVirtualMember<E, T> member, IMethod<E, T> method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public T getType()
	{
		return method.getMethodHeader().getArgumentType(1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E compile(CodePosition position, IMethodScope<E, T> scope, E value)
	{
		return method.callStatic(position, scope, Arrays.asList(member.getTarget(), value));
	}
}
