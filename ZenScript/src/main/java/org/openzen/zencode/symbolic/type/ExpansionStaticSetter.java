/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpansionStaticSetter implements ISetter
{
	private final MemberStatic member;
	private final IMethod method;
	
	public ExpansionStaticSetter(MemberStatic member, IMethod method)
	{
		this.member = member;
		this.method = method;
	}

	@Override
	public ZenType getType()
	{
		return method.getReturnType();
	}

	@Override
	public Expression compile(CodePosition position, IScopeMethod scope, Expression expression)
	{
		return method.callStatic(position, scope, expression);
	}
}
