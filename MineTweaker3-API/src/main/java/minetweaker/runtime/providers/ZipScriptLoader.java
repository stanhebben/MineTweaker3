/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime.providers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.openzen.zencode.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public class ZipScriptLoader implements IFileLoader
{
	private final ZipFile zipFile;
	private final String dir;
	
	public ZipScriptLoader(ZipFile zipFile, String dir)
	{
		this.zipFile = zipFile;
		
		if (dir.endsWith("/"))
			dir = dir.substring(0, dir.length() - 1);
		this.dir = dir;
	}

	@Override
	public InputStream load(String name) throws IOException
	{
		if (!name.startsWith("/"))
			name = "/" + name;
		
		ZipEntry entry = zipFile.getEntry(dir + name);
		if (entry == null)
			throw new FileNotFoundException("Script not found: " + name);
		
		return zipFile.getInputStream(entry);
	}
}
