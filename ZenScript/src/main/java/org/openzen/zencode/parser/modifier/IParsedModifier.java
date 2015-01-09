/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.modifier;

import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface IParsedModifier
{
	public CodePosition getPosition();
	
	public Modifier compile();
}
