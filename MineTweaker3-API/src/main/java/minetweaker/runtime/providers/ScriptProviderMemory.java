/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.runtime.providers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import minetweaker.api.MineTweakerAPI;
import minetweaker.util.FileUtil;
import minetweaker.runtime.IScriptIterator;
import minetweaker.runtime.IScriptProvider;
import org.openzen.zencode.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public class ScriptProviderMemory implements IScriptProvider
{
	public static byte[] collect(IScriptProvider provider)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			DeflaterOutputStream deflater = new DeflaterOutputStream(output);
			DataOutputStream deflaterData = new DataOutputStream(deflater);
			Set<String> executed = new HashSet<String>();

			Iterator<IScriptIterator> scripts = provider.getScripts();
			while (scripts.hasNext()) {
				IScriptIterator script = scripts.next();

				if (!executed.contains(script.getGroupName())) {
					executed.add(script.getGroupName());

					deflaterData.writeUTF(script.getGroupName());

					while (script.next()) {
						String name = script.getName();
						byte[] data = FileUtil.read(script.open());
						if (data.length == 0)
							continue; // skip empty files

						deflaterData.writeUTF(name);
						deflaterData.writeInt(data.length);
						deflaterData.write(data);
					}

					deflaterData.writeUTF("");
				}
			}

			deflaterData.writeUTF("");

			deflater.close();
		} catch (IOException ex) {
			MineTweakerAPI.logError("Could not collect scripts: " + ex.getMessage());
		}
		return output.toByteArray();
	}

	private final Map<String, MemoryModule> modules;

	public ScriptProviderMemory(byte[] scripts)
	{
		modules = new HashMap<String, MemoryModule>();

		try {
			InflaterInputStream inflater = new InflaterInputStream(new ByteArrayInputStream(scripts));
			DataInputStream inflaterData = new DataInputStream(inflater);

			String moduleName = inflaterData.readUTF();
			while (moduleName.length() > 0) {
				List<MemoryFile> files = new ArrayList<MemoryFile>();

				String fileName = inflaterData.readUTF();
				while (fileName.length() > 0) {
					byte[] data = new byte[inflaterData.readInt()];
					inflaterData.readFully(data);
					files.add(new MemoryFile(fileName, data));

					fileName = inflaterData.readUTF();
				}
				modules.put(moduleName, new MemoryModule(moduleName, files));

				moduleName = inflaterData.readUTF();
			}

			inflaterData.close();
		} catch (IOException ex) {
			MineTweakerAPI.logError("Could not load transmitted scripts: " + ex.getMessage());
		}
	}

	@Override
	public Iterator<IScriptIterator> getScripts()
	{
		return new ProviderIterator();
	}

	private class ProviderIterator implements Iterator<IScriptIterator>
	{
		private final Iterator<MemoryModule> baseIterator = modules.values().iterator();

		@Override
		public boolean hasNext()
		{
			return baseIterator.hasNext();
		}

		@Override
		public IScriptIterator next()
		{
			return new ScriptIterator(baseIterator.next());
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}

	private class ScriptIterator implements IScriptIterator
	{
		private final MemoryModule module;
		private final Iterator<MemoryFile> files;
		private final IFileLoader fileLoader;
		private MemoryFile current;

		public ScriptIterator(MemoryModule module)
		{
			this.module = module;
			files = module.data.iterator();
			this.fileLoader = new MemoryModuleFileLoader(module);
			current = null;
		}

		@Override
		public String getGroupName()
		{
			return module.name;
		}
		
		@Override
		public IFileLoader getGroupScriptLoader()
		{
			return fileLoader;
		}

		@Override
		public boolean next()
		{
			if (files.hasNext()) {
				current = files.next();
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
			return new ByteArrayInputStream(current.data);
		}
	}

	private class MemoryModule
	{
		private final String name;
		private final List<MemoryFile> data;
		private final Map<String, MemoryFile> files;

		public MemoryModule(String name, List<MemoryFile> data)
		{
			this.name = name;
			this.data = data;
			this.files = new HashMap<String, MemoryFile>();
			
			for (MemoryFile file : data) {
				files.put(file.name, file);
			}
		}
	}

	private class MemoryFile
	{
		private final String name;
		private final byte[] data;

		public MemoryFile(String name, byte[] data)
		{
			this.name = name;
			this.data = data;
		}
	}
	
	private class MemoryModuleFileLoader implements IFileLoader
	{
		private final MemoryModule module;
		
		public MemoryModuleFileLoader(MemoryModule module)
		{
			this.module = module;
		}

		@Override
		public InputStream load(String name) throws IOException
		{
			if (!module.files.containsKey(name))
				throw new FileNotFoundException("Script not found: " + name);
			
			return new ByteArrayInputStream(module.files.get(name).data);
		}
	}
}
