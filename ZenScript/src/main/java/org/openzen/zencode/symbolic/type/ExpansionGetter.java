/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Collections;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialVirtualMember;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ExpansionGetter<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		implements IGetter<E, T>
{
	private final PartialVirtualMember<E, T> member;
	private final IMethod<E, T> method;
	
	public ExpansionGetter(PartialVirtualMember<E, T> member, IMethod<E, T> method)
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
	public IPartialExpression<E, T> compileGet(CodePosition position, IMethodScope<E, T> scope)
	{
		return method.callStatic(position, scope, Collections.singletonList(member.getTarget()));
	}
}
