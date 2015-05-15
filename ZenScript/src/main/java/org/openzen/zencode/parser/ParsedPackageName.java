/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser;

import java.util.List;
import org.openzen.zencode.util.Strings;

/**
 *
 * @author Stan
 */
public class ParsedPackageName
{
	private final List<String> nameParts;
	
	public ParsedPackageName(List<String> nameParts)
	{
		this.nameParts = nameParts;
	}
	
	public List<String> getNameParts()
	{
		return nameParts;
	}
	
	public String getName()
	{
		return Strings.join(nameParts, ".");
	}
}
