/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.expression.partial.PartialStaticMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class MemberStaticSymbol<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		implements IZenSymbol<E, T>
{
	private final PartialStaticMember<E, T> member;

	public MemberStaticSymbol(PartialStaticMember<E, T> member)
	{
		this.member = member;
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IMethodScope<E, T> environment)
	{
		return member.makeVariant(position, environment);
	}
}
