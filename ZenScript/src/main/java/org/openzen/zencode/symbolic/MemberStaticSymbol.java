/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class MemberStaticSymbol implements IZenSymbol
{
	private final MemberStatic member;
	
	public MemberStaticSymbol(MemberStatic member) {
		this.member = member;
	}

	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod environment)
	{
		return member.makeVariant(position, environment);
	}
}
