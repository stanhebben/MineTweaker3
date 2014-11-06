/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface IGetter
{
	public ZenType getType();
	
	public IPartialExpression compileGet(CodePosition position, IScopeMethod scope);
}
