/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.runtime;

import java.io.IOException;
import java.io.InputStream;
import zenscript.parser.IFileLoader;

/**
 *
 * @author Stan
 */
public interface IScriptIterator {
	public String getGroupName();
	
	public IFileLoader getGroupFileLoader();
	
	public boolean next();
	
	public String getName();
	
	public InputStream open() throws IOException;
}
