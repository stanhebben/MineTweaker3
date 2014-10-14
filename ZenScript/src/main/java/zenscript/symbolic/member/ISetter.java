/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.member;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public interface ISetter
{
	public ZenType getType();
	
	public Expression compile(ZenPosition position, IScopeMethod scope, Expression value);
}
