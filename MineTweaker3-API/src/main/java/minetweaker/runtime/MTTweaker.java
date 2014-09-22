/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.runtime;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.runtime.providers.ScriptProviderMemory;
import stanhebben.zenscript.compiler.IScopeGlobal;
import zenscript.lexer.ParseException;
import zenscript.lexer.ZenTokener;
import zenscript.parser.ParsedFile;
import zenscript.parser.ParsedModule;
import zenscript.parser.ParserEnvironment;

/**
 * 
 * 
 * @author Stan Hebben
 */
public final class MTTweaker {
	private static final boolean DEBUG = false;
	
	private final List<IUndoableAction> actions = new ArrayList<IUndoableAction>();
	private final Set<IUndoableAction> wereStuck = new LinkedHashSet<IUndoableAction>();
	private final Map<Object, IUndoableAction> stuckOverridable = new HashMap<Object, IUndoableAction>();
	
	private IScriptProvider scriptProvider;
	private byte[] scriptData;
	
	public void apply(IUndoableAction action) {
		MineTweakerAPI.logInfo(action.describe());
		
		Object overrideKey = action.getOverrideKey();
		if (wereStuck.contains(action)) {
			wereStuck.remove(action);
			
			if (overrideKey != null) {
				stuckOverridable.remove(overrideKey);
			}
		} else {
			if (overrideKey != null && stuckOverridable.containsKey(overrideKey)) {
				wereStuck.remove(stuckOverridable.get(overrideKey));
				stuckOverridable.remove(overrideKey);
			}
			
			action.apply();
		}
		
		actions.add(action);
	}
	
	public void remove(IIngredient items) {
		GlobalRegistry.remove(items);
	}
	
	public List<IUndoableAction> rollback() {
		List<IUndoableAction> stuck = new ArrayList<IUndoableAction>();
		for (int i = actions.size() - 1; i >= 0; i--) {
			IUndoableAction action = actions.get(i);
			if (action.canUndo()) {
				MineTweakerAPI.logInfo(action.describeUndo());
				action.undo();
			} else {
				MineTweakerAPI.logInfo("[Stuck] " + action.describe());
				stuck.add(0, action);
				wereStuck.add(action);
				
				Object overrideKey = action.getOverrideKey();
				if (overrideKey != null) {
					stuckOverridable.put(overrideKey, action);
				}
			}
		}
		actions.clear();
		return stuck;
	}
	
	public void setScriptProvider(IScriptProvider provider) {
		scriptProvider = provider;
	}
	
	public void load() {
		System.out.println("Loading scripts");
		
		scriptData = ScriptProviderMemory.collect(scriptProvider);
		Set<String> executed = new HashSet<String>();
		
		Map<String, byte[]> classes = new HashMap<String, byte[]>();
		IScopeGlobal global = GlobalRegistry.makeGlobalEnvironment(classes);
		
		// Step 1: parse all files
		ParserEnvironment parserEnvironment = new ParserEnvironment(global.getEnvironment());
		Iterator<IScriptIterator> scripts = scriptProvider.getScripts();
		while (scripts.hasNext()) {
			IScriptIterator script = scripts.next();

			if (!executed.contains(script.getGroupName())) {
				executed.add(script.getGroupName());
				
				ParsedModule module = parserEnvironment.makeModule(script.getGroupName(), script.getGroupFileLoader());
				
				List<ParsedFile> files = new ArrayList<ParsedFile>();

				while (script.next()) {
					Reader reader = null;
					try {
						reader = new InputStreamReader(new BufferedInputStream(script.open()));
						
						String filename = script.getName();
						
						ZenTokener parser = new ZenTokener(reader);
						ParsedFile pfile = new ParsedFile(module, filename, parser);
						files.add(pfile);
					} catch (IOException ex) {
						MineTweakerAPI.logError("Could not load script " + script.getName() + ": " + ex.getMessage());
					} catch (ParseException ex) {
						MineTweakerAPI.logError("Error parsing " + ex.getPosition() + ": " + ex.getExplanation());
					} catch (Throwable ex) {
						MineTweakerAPI.logError("Error loading " + script.getName() + ": " + ex.toString());
						ex.printStackTrace();
					}
					
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException ex) {}
					}
				}

/*				try {
					String filename = script.getGroupName();
					System.out.println("MineTweaker: Loading " + filename);
					compileScripts(filename, files, environmentGlobal, DEBUG);

					// execute scripts
					ZenModule cmodule = new ZenModule(classes, MineTweakerAPI.class.getClassLoader());
					cmodule.getMain().run();
				} catch (Throwable ex) {
					MineTweakerAPI.logError("Error executing " + script.getGroupName() + ": " + ex.getMessage());
					ex.printStackTrace();
				}*/
			}
		}
		
		// Step 2: compile all files
		
		
		// Step 3: execute
		
		if (wereStuck.size() > 0) {
			MineTweakerAPI.logWarning(Integer.toString(wereStuck.size()) + " modifications were stuck");
			for (IUndoableAction action : wereStuck) {
				MineTweakerAPI.logInfo("Stuck: " + action.describe());
			}
		}
	}
	
	public byte[] getScriptData() {
		return scriptData;
	}
}
