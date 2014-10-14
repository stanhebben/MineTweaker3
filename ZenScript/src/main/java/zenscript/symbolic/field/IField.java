/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.field;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface IField
{
	public ZenType getType();
	
	public boolean isFinal();
	
	public boolean isStatic();
	
	public void compileStaticGet(MethodOutput output);
	
	public void compileStaticSet(MethodOutput output);
	
	public void compileInstanceGet(MethodOutput output);
	
	public void compileInstanceSet(MethodOutput output);
}
