/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.method;

import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public abstract class AbstractMethod implements IMethod
{
	@Override
	public MethodHeader getMethodHeader()
	{
		return getFunctionType().getHeader();
	}
	
	@Override
	public ZenType getReturnType()
	{
		return getMethodHeader().getReturnType();
	}
}
