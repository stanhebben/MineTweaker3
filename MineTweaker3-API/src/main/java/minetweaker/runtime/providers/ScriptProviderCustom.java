/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.runtime.providers;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import minetweaker.runtime.IScriptIterator;
import minetweaker.runtime.IScriptProvider;
import org.openzen.zencode.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public class ScriptProviderCustom implements IScriptProvider
{
	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final String moduleName;
	private final List<CustomScript> scripts;
	private final Map<String, CustomScript> scriptByName;
	private final IFileLoader fileLoader;

	public ScriptProviderCustom(String moduleName)
	{
		this.moduleName = moduleName;
		
		scripts = new ArrayList<CustomScript>();
		scriptByName = new HashMap<String, CustomScript>();
		fileLoader = new CustomFileLoader();
	}

	public void add(String name, byte[] content)
	{
		CustomScript script = new CustomScript(name, content);

		scripts.add(script);
		scriptByName.put(name, script);
	}

	public void add(String name, String content)
	{
		add(name, content.getBytes(UTF8));
	}

	@Override
	public Iterator<IScriptIterator> getScripts()
	{
		return Collections.<IScriptIterator>singleton(new CustomScriptIterator()).iterator();
	}

	private class CustomScript
	{
		private final String name;
		private final byte[] content;

		public CustomScript(String name, byte[] content)
		{
			this.name = name;
			this.content = content;
		}
	}
	
	private class CustomFileLoader implements IFileLoader
	{

		@Override
		public InputStream load(String name) throws IOException
		{
			if (!scriptByName.containsKey(name))
				throw new FileNotFoundException("Script not found: " + name);
			
			return new ByteArrayInputStream(scriptByName.get(name).content);
		}
	}

	private class CustomScriptIterator implements IScriptIterator
	{
		private CustomScript current;
		private final Iterator<CustomScript> iterator = scripts.iterator();

		@Override
		public String getGroupName()
		{
			return moduleName;
		}

		@Override
		public IFileLoader getGroupScriptLoader()
		{
			return fileLoader;
		}

		@Override
		public boolean next()
		{
			if (iterator.hasNext()) {
				current = iterator.next();
				return true;
			} else
				return false;
		}

		@Override
		public String getName()
		{
			return current.name;
		}

		@Override
		public InputStream open() throws IOException
		{
			return new ByteArrayInputStream(current.content);
		}
	}
}
