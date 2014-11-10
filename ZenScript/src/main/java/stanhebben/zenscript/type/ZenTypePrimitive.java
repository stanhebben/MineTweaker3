/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.type;

import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;

/**
 *
 * @author Stan
 */
public abstract class ZenTypePrimitive extends ZenType
{
	public ZenTypePrimitive(IScopeGlobal scope)
	{
		super(scope);
	}
	
	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		MemberVirtual result = new MemberVirtual(position, environment, value.eval(), name);
		memberExpansion(result);
		if (result.isEmpty()) {
			environment.error(position, getName() + " value has no member named " + name);
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		MemberStatic result = new MemberStatic(position, environment, this, name);
		staticMemberExpansion(result);
		if (result.isEmpty()) {
			environment.error(position, getName() + " value has no static member named " + name);
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}
}
