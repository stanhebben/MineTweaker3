/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.util;

import java.lang.reflect.Modifier;

/**
 *
 * @author Stan
 */
public class JavaUtil
{
	private JavaUtil() {}
	
	public static int getZCModifiers(int javaModifiers)
	{
		int result = 0;
		
		if ((javaModifiers & Modifier.ABSTRACT) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.ABSTRACT.getCode();
		if ((javaModifiers & Modifier.FINAL) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.FINAL.getCode();
		if ((javaModifiers & Modifier.NATIVE) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.NATIVE.getCode();
		if ((javaModifiers & Modifier.PRIVATE) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.PRIVATE.getCode();
		if ((javaModifiers & Modifier.PROTECTED) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.PRIVATE.getCode();
		if ((javaModifiers & Modifier.PUBLIC) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.EXPORT.getCode();
		if ((javaModifiers & Modifier.STATIC) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.STATIC.getCode();
		if ((javaModifiers & Modifier.SYNCHRONIZED) > 0)
			result |= org.openzen.zencode.symbolic.Modifier.SYNCHRONIZED.getCode();
		
		return result;
	}
}
