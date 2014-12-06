/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode;

import java.util.Map;

/**
 *
 * @author Stan
 */
public class ZenClassLoader extends ClassLoader {
	private final Map<String, byte[]> classes;
	
	public ZenClassLoader(ClassLoader baseClassLoader, Map<String, byte[]> classes) {
		super(baseClassLoader);
		
		this.classes = classes;
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		if (classes.containsKey(name))
			return defineClass(name, classes.get(name), 0, classes.get(name).length);

		return super.findClass(name);
	}
}
