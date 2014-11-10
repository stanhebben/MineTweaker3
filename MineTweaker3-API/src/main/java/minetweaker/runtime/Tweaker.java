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
import minetweaker.api.action.IUndoableAction;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.runtime.providers.ScriptProviderMemory;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.parser.ParsedFile;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.parser.ParserEnvironment;

/**
 *
 *
 * @author Stan Hebben
 */
public final class Tweaker
{
	private static final boolean DEBUG = false;

	private final List<IUndoableAction> actions = new ArrayList<IUndoableAction>();
	private final Set<IUndoableAction> wereStuck = new LinkedHashSet<IUndoableAction>();
	private final Map<Object, IUndoableAction> stuckOverridable = new HashMap<Object, IUndoableAction>();

	private IScriptProvider scriptProvider;
	private byte[] scriptData;

	public void apply(IUndoableAction action)
	{
		MineTweakerAPI.logInfo(action.describe());
		
		if (wereStuck.contains(action))
			wereStuck.remove(action);
		else
			action.apply();

		actions.add(action);
	}

	public void remove(IIngredient items)
	{
		GlobalRegistry.remove(items);
	}

	public List<IUndoableAction> rollback()
	{
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
			}
		}
		actions.clear();
		return stuck;
	}

	public void setScriptProvider(IScriptProvider provider)
	{
		scriptProvider = provider;
	}

	public void load()
	{
		System.out.println("Loading scripts");

		scriptData = ScriptProviderMemory.collect(scriptProvider);
		Set<String> executed = new HashSet<String>();

		Map<String, byte[]> classes = new HashMap<String, byte[]>();
		IScopeGlobal global = GlobalRegistry.makeGlobalEnvironment(classes);

		// Step 1: parse all files
		ParserEnvironment parserEnvironment = new ParserEnvironment(global, classes);
		Iterator<IScriptIterator> scripts = scriptProvider.getScripts();
		while (scripts.hasNext()) {
			IScriptIterator script = scripts.next();

			if (!executed.contains(script.getGroupName())) {
				executed.add(script.getGroupName());
				
				ParsedModule module = new ParsedModule(parserEnvironment, script.getGroupScriptLoader(), script.getGroupName());
				parserEnvironment.addModule(module);
				
				while (script.next()) {
					Reader reader = null;
					try {
						reader = new InputStreamReader(new BufferedInputStream(script.open()));

						String filename = script.getName();

						ZenLexer parser = new ZenLexer(reader);
						ParsedFile pfile = new ParsedFile(module, filename, parser);
						module.addScript(pfile);
					} catch (IOException ex) {
						MineTweakerAPI.logError("Could not load script " + script.getName() + ": " + ex.getMessage());
					} catch (ParseException ex) {
						//ex.printStackTrace();
						MineTweakerAPI.logError("Error parsing " + ex.getPosition().getFile().getFileName() + ":" + ex.getPosition().getLine() + " -- " + ex.getExplanation());
					} catch (Exception ex) {
						MineTweakerAPI.logError("Error loading " + script.getName() + ": " + ex.toString(), ex);
					}

					if (reader != null) {
						try {
							reader.close();
						} catch (IOException ex) {
						}
					}
				}
			}
		}

		// Step 2: compile all files
		Runnable compiled = parserEnvironment.compile();
		
		// Step 3: execute
		compiled.run();
		
		if (wereStuck.size() > 0) {
			MineTweakerAPI.logWarning(Integer.toString(wereStuck.size()) + " modifications were stuck");
			for (IUndoableAction action : wereStuck) {
				MineTweakerAPI.logInfo("Stuck: " + action.describe());
			}
		}
	}

	public byte[] getScriptData()
	{
		return scriptData;
	}
}
