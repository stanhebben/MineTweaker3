/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.expression.partial.PartialVirtualMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class MemberVirtualSymbol<E extends IPartialExpression<E>> implements IZenSymbol<E>
{
	private final PartialVirtualMember<E> member;

	public MemberVirtualSymbol(PartialVirtualMember<E> member)
	{
		this.member = member;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> environment)
	{
		return member.makeVariant(position, environment);
	}
}
