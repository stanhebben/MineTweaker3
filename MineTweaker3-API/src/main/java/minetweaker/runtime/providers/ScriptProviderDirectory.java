/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.runtime.providers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import minetweaker.MineTweakerAPI;
import minetweaker.runtime.IScriptIterator;
import minetweaker.runtime.IScriptProvider;

/**
 *
 * @author Stan
 */
public class ScriptProviderDirectory implements IScriptProvider {
    public final static String SORTING_FLAG = ".sorting";
	private final File directory;

	public ScriptProviderDirectory(File directory) {
		if (directory == null)
			throw new IllegalArgumentException("directory cannot be null");

		this.directory = directory;
	}

    protected File[] getFiles()
    {
      if( directory.exists() )
      {
          //Build path to flag file
          StringBuilder sortFlag = new StringBuilder();
          sortFlag.append(directory.getAbsolutePath());
          sortFlag.append("/");
          sortFlag.append(SORTING_FLAG);
          
          File file = new File( sortFlag.toString() );
          File files[] = directory.listFiles();

          //Sorting flag file exists
          if( file.exists() )
          {
              Arrays.sort(files);
          }
          return files;
      }
      else
      {
          return null;
      }
    }
            
	@Override
	public Iterator<IScriptIterator> getScripts() {
		List<IScriptIterator> scripts = new ArrayList<IScriptIterator>();
        File files[] = this.getFiles();
        
        if( files != null )
        {
			for (File file : files) {
				if (file.isDirectory()) {
					scripts.add(new ScriptIteratorDirectory(file));
				} else if (file.getName().endsWith(".zs")) {
					scripts.add(new ScriptIteratorSingle(file));
				} else if (file.getName().endsWith(".zip")) {
					try {
						scripts.add(new ScriptIteratorZip(file));
					} catch (IOException ex) {
						MineTweakerAPI.logError("Could not load " + file.getName() + ": " + ex.getMessage());
					}
				}
			}
        }
		return scripts.iterator();
	}
}
