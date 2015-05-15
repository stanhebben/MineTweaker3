/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.library;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.openzen.zencode.annotations.OptionalInt;
import org.openzen.zencode.annotations.ZenExpansion;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;

/**
 *
 * @author Stan
 */
@ZenExpansion("string")
public class StringExpansion
{
	@ZenMethod
	public static List<String> split(String value, String separator, @OptionalInt(Integer.MAX_VALUE) int maximum)
	{
		List<String> result = new ArrayList<>();
		int minIndex = 0;
		
		while (minIndex + separator.length() <= value.length()) {
			int index = value.indexOf(separator, minIndex);
			if (index < 0)
				break;
			
			result.add(value.substring(minIndex, index));
			minIndex = index + separator.length();
			
			if (result.size() >= maximum)
				break;
		}
		
		result.add(value.substring(minIndex));
		return result;
	}
	
	@ZenMethod
	public static List<String> split(String value, char separator, @OptionalInt(Integer.MAX_VALUE) int maximum)
	{
		List<String> result = new ArrayList<>();
		int minIndex = 0;
		
		while (minIndex < value.length()) {
			int index = value.indexOf(separator, minIndex);
			if (index < 0)
				break;
			
			result.add(value.substring(minIndex, index));
			minIndex = index + 1;
			
			if (result.size() >= maximum)
				break;
		}
		
		result.add(value.substring(minIndex));
		return result;
	}
	
	@ZenMethod
	public static int indexOf(String value, String needle)
	{
		return value.indexOf(needle);
	}
	
	@ZenMethod
	public static int indexOf(String value, char needle)
	{
		return value.indexOf(needle);
	}
	
	@ZenMethod
	public static int indexOf(String value, String needle, int fromIndex)
	{
		return value.indexOf(needle, fromIndex);
	}
	
	@ZenMethod
	public static int indexOf(String value, char needle, int fromIndex)
	{
		return value.indexOf(needle, fromIndex);
	}
	
	@ZenMethod
	public static int lastIndexOf(String value, String needle)
	{
		return value.lastIndexOf(needle);
	}
	
	@ZenMethod
	public static int lastIndexOf(String value, char needle)
	{
		return value.lastIndexOf(needle);
	}
	
	@ZenMethod
	public static int lastIndexOf(String value, String needle, int fromIndex)
	{
		return value.lastIndexOf(needle, fromIndex);
	}
	
	@ZenMethod
	public static int lastIndexOf(String value, char needle, int fromIndex)
	{
		return value.lastIndexOf(needle, fromIndex);
	}
	
	@ZenMethod
	public static String from(String value, String needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(index);
		else
			return null;
	}
	
	@ZenMethod
	public static String from(String value, char needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(value.indexOf(needle));
		else
			return null;
	}
	
	@ZenMethod
	public static String fromLast(String value, String needle)
	{
		int index = value.lastIndexOf(needle);
		if (index >= 0)
			return value.substring(index);
		else
			return null;
	}
	
	@ZenMethod
	public static String fromLast(String value, char needle)
	{
		int index = value.lastIndexOf(needle);
		if (index >= 0)
			return value.substring(index);
		else
			return null;
	}
	
	@ZenMethod
	public static String after(String value, String needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(index + needle.length());
		else
			return null;
	}
	
	@ZenMethod
	public static String after(String value, char needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(index + 1);
		else
			return null;
	}
	
	@ZenMethod
	public static String afterLast(String value, String needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(index + needle.length());
		else
			return null;
	}
	
	@ZenMethod
	public static String afterLast(String value, char needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(index + 1);
		else
			return null;
	}
	
	@ZenMethod
	public static String until(String value, String needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(0, index);
		else
			return null;
	}
	
	@ZenMethod
	public static String until(String value, char needle)
	{
		int index = value.indexOf(needle);
		if (index >= 0)
			return value.substring(0, index);
		else
			return null;
	}
	
	@ZenMethod
	public static String untilLast(String value, String needle)
	{
		int index = value.lastIndexOf(needle);
		if (index >= 0)
			return value.substring(0, index);
		else
			return null;
	}
	
	@ZenMethod
	public static String untilLast(String value, char needle)
	{
		int index = value.lastIndexOf(needle);
		if (index >= 0)
			return value.substring(0, index);
		else
			return null;
	}
	
	private static final Pattern PATTERN_INT = Pattern.compile("-?[0-9]+");
	@ZenGetter
	public static boolean isInt(String value)
	{
		return PATTERN_INT.matcher(value).matches();
	}
	
	private static final Pattern PATTERN_FLOAT;
	static
	{
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";
		// an exponent is 'e' or 'E' followed by an optionally 
        // signed decimal integer.
        final String Exp = "[eE][+-]?"+Digits;
        final String fpRegex = ("[+-]?(NaN|Infinity|(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|"
				+ "(\\.("+Digits+")("+Exp+")?)|"
				+ "((" + "(0[xX]" + HexDigits + "(\\.)?)|" + "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")"
				+ ")[pP][+-]?" + Digits + "))" + "))");
        
		PATTERN_FLOAT = Pattern.compile(fpRegex);
	}
	
	@ZenGetter
	public static boolean isFloat(String value)
	{
		return PATTERN_FLOAT.matcher(value).matches();
	}
	
	private static final Pattern PATTERN_IDENTIFIER = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");
	@ZenGetter
	public static boolean isIdentifier(String value)
	{
		return PATTERN_IDENTIFIER.matcher(value).matches();
	}
	
	@ZenGetter
	public static String lowercase(String value)
	{
		return value.toLowerCase();
	}
	
	@ZenGetter
	public static String uppercase(String value)
	{
		return value.toUpperCase();
	}
	
	@ZenGetter
	public static String withoutAccents(String value)
	{
		return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
	}
	
	@ZenGetter
	public static String trimmed(String value)
	{
		return value.trim();
	}
	
	@ZenMethod
	public static String repeat(String value, int count)
	{
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < count; i++) {
			result.append(value);
		}
		return result.toString();
	}
	
	@ZenMethod
	public static String padStart(String value, int minLength, char padding)
	{
		if (value.length() >= minLength)
			return value;
		
		StringBuilder result = new StringBuilder();
		for (int i = value.length(); i < minLength; i++)
			result.append(padding);
		
		result.append(value);
		return result.toString();
	}
	
	@ZenMethod
	public static String padEnd(String value, int minLength, char padding)
	{
		if (value.length() >= minLength)
			return value;
		
		StringBuilder result = new StringBuilder();
		result.append(value);
		for (int i = value.length(); i < minLength; i++)
			result.append(padding);
		
		return result.toString();
	}
	
	@ZenMethod
	public static String commonPrefix(String value, String other)
	{
		int maxPrefixLength = Math.min(value.length(), other.length());
		
		int length = 0;
		while (length < maxPrefixLength && value.charAt(length) == other.charAt(length))
			length++;
		
		return value.substring(0, length);
	}
	
	@ZenMethod
	public static String commonSuffix(String value, String other)
	{
		int maxSuffixLength = Math.min(value.length(), other.length());
		
		int lengthPlusOne = 1;
		while (lengthPlusOne < maxSuffixLength
				&& value.charAt(value.length() - lengthPlusOne) == other.charAt(other.length() - lengthPlusOne))
			lengthPlusOne++;
		
		return value.substring(value.length() - lengthPlusOne + 1);
	}
}
