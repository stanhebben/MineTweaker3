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
public class ParsedModifierOpen implements IParsedModifier
{
	private final CodePosition position;
	
	public ParsedModifierOpen(CodePosition position)
	{
		this.position = position;
	}

	@Override
	public CodePosition getPosition()
	{
		return position;
	}

	@Override
	public Modifier compile()
	{
		return Modifier.OPEN;
	}
}
