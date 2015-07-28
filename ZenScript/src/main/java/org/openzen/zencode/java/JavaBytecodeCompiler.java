/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Stan
 */
public class JavaBytecodeCompiler
{
	private Map<String, byte[]> classes;
	
	public JavaBytecodeCompiler()
	{
		this.classes = new HashMap<>();
	}
	
	public boolean hasClass(String name)
	{
		return classes.containsKey(name);
	}
	
	public void putClass(String name, byte[] bytecode)
	{
		classes.put(name, bytecode);
	}
	
	public byte[] getClass(String name)
	{
		return classes.get(name);
	}
	
	public Map<String, byte[]> getClasses()
	{
		return classes;
	}

	public void writeClassesToFolder(File outputDirectory)
	{
		for (Map.Entry<String, byte[]> classEntry : classes.entrySet()) {
			File outputFile = new File(outputDirectory, classEntry.getKey().replace('.', '/') + ".class");
			if (!outputFile.getParentFile().exists())
				outputFile.getParentFile().mkdirs();

			try {
				Files.write(outputFile.toPath(), classEntry.getValue());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
