/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedAccessor
{
	private final CodePosition position;
	private final Type type;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	
	public ParsedAccessor(
			CodePosition position,
			Type type,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers)
	{
		this.position = position;
		this.type = type;
		this.annotations = annotations;
		this.modifiers = modifiers;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public Type getType()
	{
		return type;
	}

	public List<ParsedAnnotation> getAnnotations()
	{
		return annotations;
	}

	public List<IParsedModifier> getModifiers()
	{
		return modifiers;
	}
	
	public static enum Type {
		GET,
		SET
	}
}
