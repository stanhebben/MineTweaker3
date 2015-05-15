/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.library;

import java.util.List;
import org.openzen.zencode.annotations.OptionalString;
import org.openzen.zencode.annotations.ZenExpansion;
import org.openzen.zencode.annotations.ZenMethod;

/**
 *
 * @author Stan
 */
@ZenExpansion("string[]")
public class StringArrayExpansion
{
	@ZenMethod
	public String join(String[] array, @OptionalString("") String delimiter)
	{
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				result.append(delimiter);
			
			result.append(array[i]);
		}
		return result.toString();
	}
	
	@ZenMethod
	public String join(List<String> array, @OptionalString("") String delimiter)
	{
		boolean first = true;
		StringBuilder result = new StringBuilder();
		for (String value : array) {
			if (first) {
				first = false;
			} else {
				result.append(delimiter);
			}
			
			result.append(value);
		}
		return result.toString();
	}
}
