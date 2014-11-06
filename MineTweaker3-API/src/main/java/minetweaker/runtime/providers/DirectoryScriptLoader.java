/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.providers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.openzen.zencode.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public class DirectoryScriptLoader implements IFileLoader
{
	private final File directory;
	
	public DirectoryScriptLoader(File directory)
	{
		this.directory = directory;
	}

	@Override
	public InputStream load(String name) throws IOException
	{
		File file = new File(directory, name);
		if (!file.exists())
			throw new FileNotFoundException("Could not find script " + name);
		
		return new BufferedInputStream(new FileInputStream(file));
	}
}
