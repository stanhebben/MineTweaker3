/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import org.openzen.zencode.parser.statement.ParsedStatement;

/**
 *
 * @author Stan
 */
public class ParsedStaticInitializerMember implements IParsedMember
{
	private final ParsedStatement contents;
	
	public ParsedStaticInitializerMember(ParsedStatement contents)
	{
		this.contents = contents;
	}
}
