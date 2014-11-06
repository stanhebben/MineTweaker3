/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.util;

/**
 *
 * @author Stan
 */
public class Modifiers
{
	private Modifiers() {}
	
	public static final int ACCESS_EXPORT = 1;
	public static final int ACCESS_PUBLIC = 2;
	public static final int ACCESS_PRIVATE = 4;
	public static final int FINAL = 8;
	public static final int ABSTRACT = 16;
	public static final int STATIC = 32;
	public static final int OVERRIDE = 64;
}
