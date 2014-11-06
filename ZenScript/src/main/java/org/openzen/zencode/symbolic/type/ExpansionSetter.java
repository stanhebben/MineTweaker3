/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpansionSetter implements ISetter
{
	private final MemberVirtual member;
	private final IMethod method;
	
	public ExpansionSetter(MemberVirtual member, IMethod method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public ZenType getType()
	{
		return method.getMethodHeader().getArgumentType(1);
	}

	@Override
	public Expression compile(CodePosition position, IScopeMethod scope, Expression value)
	{
		return method.call(position, scope, member.getTarget(), value);
	}
}
