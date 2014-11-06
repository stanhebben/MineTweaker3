/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.runtime.providers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import minetweaker.runtime.IScriptIterator;
import org.openzen.zencode.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public class ScriptIteratorSingle implements IScriptIterator
{
	private final File file;
	private final IFileLoader scriptLoader;
	private boolean first = true;

	public ScriptIteratorSingle(File file)
	{
		this.file = file;
		scriptLoader = new DirectoryScriptLoader(file.getParentFile());
	}

	@Override
	public String getGroupName()
	{
		return file.getName();
	}

	@Override
	public IFileLoader getGroupScriptLoader()
	{
		return scriptLoader;
	}

	@Override
	public boolean next()
	{
		if (first) {
			first = false;
			return true;
		} else
			return false;
	}

	@Override
	public String getName()
	{
		return file.getName();
	}

	@Override
	public InputStream open() throws IOException
	{
		return new BufferedInputStream(new FileInputStream(file));
	}
}
