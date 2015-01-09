/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.List;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.parser.modifier.IParsedModifier;

/**
 *
 * @author Stan
 */
public enum Modifier
{
	ABSTRACT(1, "abstract", 4 + 8 + 16 + 32),
	EXPORT(2, "export", 128 + 256),
	FINAL(4, "final", 1 + 512),
	GENERATED(8, "generated", 1 + 16 + 32),
	NATIVE(16, "native", 1 + 8 + 32),
	OPEN(32, "open", 1 + 8 + 16),
	OVERRIDE(64, "override", 128),
	PRIVATE(128, "private", 2 + 256),
	PUBLIC(256, "public", 2 + 128),
	STATIC(512, "static", 1 + 4 + 64),
	SYNCHRONIZED(1024, "synchronized", 0);
	
	private final int code;
	private final String name;
	private final int incompatibleWith;
	
	private Modifier(int code, String name, int incompatibleWith)
	{
		this.code = code;
		this.name = name;
		this.incompatibleWith = incompatibleWith;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCode()
	{
		return code;
	}
	
	public static int compileModifiers(List<IParsedModifier> modifiers, ICodeErrorLogger<?, ?> errorLogger)
	{
		int result = 0;
		
		for (IParsedModifier modifier : modifiers) {
			Modifier compiled = modifier.compile();
			if ((result & compiled.code) > 0)
				errorLogger.errorDuplicateModifier(modifier.getPosition(), compiled);
			else if ((result & compiled.incompatibleWith) > 0)
				errorLogger.errorIncompatibleModifier(
						modifier.getPosition(),
						getModifier(result & compiled.incompatibleWith),
						compiled);
			else
				result |= compiled.code;
		}
		
		return result;
	}
	
	private static Modifier getModifier(int code)
	{
		for (Modifier modifier : values()) {
			if ((code & modifier.code) > 0)
				return modifier;
		}
		
		return null;
	}
}
