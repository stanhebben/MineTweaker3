/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.symbols.IZenSymbol;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class MemberVirtualSymbol implements IZenSymbol
{
	private final MemberVirtual member;
	
	public MemberVirtualSymbol(MemberVirtual member) {
		this.member = member;
	}

	@Override
	public IPartialExpression instance(ZenPosition position, IScopeMethod environment)
	{
		return member.makeVariant(position, environment);
	}
}
