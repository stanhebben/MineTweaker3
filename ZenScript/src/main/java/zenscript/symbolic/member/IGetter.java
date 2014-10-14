/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.member;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public interface IGetter
{
	public ZenType getType();
	
	public IPartialExpression compileGet(ZenPosition position, IScopeMethod scope);
}
