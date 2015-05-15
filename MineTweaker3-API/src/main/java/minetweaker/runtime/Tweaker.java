/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.runtime;

import java.io.BufferedInputStream;
import java.io.File;
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
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.lexer.ParseException;
import org.openzen.zencode.lexer.ZenLexer;
import org.openzen.zencode.parser.ParsedFile;
import org.openzen.zencode.parser.ParsedModule;
import org.openzen.zencode.java.JavaCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;

/**
 *
 *
 * @author Stan Hebben
 */
public final class Tweaker
{
	private static final boolean DEBUG = true;

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
		
		IZenCompileEnvironment<IJavaExpression> environment = GlobalRegistry.createGlobalEnvironment();

		// Step 1: parse all files
		JavaCompiler compiler = new JavaCompiler(environment);
		
		if (DEBUG)
			compiler.setDebugOutputDirectory(new File("scripts-debug"));
		
		Iterator<IScriptIterator> scripts = scriptProvider.getScripts();
		while (scripts.hasNext()) {
			IScriptIterator script = scripts.next();

			if (!executed.contains(script.getGroupName())) {
				executed.add(script.getGroupName());
				
				ParsedModule module = compiler.createAndAddModule(script.getGroupName(), script.getGroupScriptLoader());
				
				while (script.next()) {
					Reader reader = null;
					try {
						reader = new InputStreamReader(new BufferedInputStream(script.open()));

						String filename = script.getName();

						ZenLexer parser = new ZenLexer(environment.getErrorLogger(), reader);
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

		if (!environment.getErrorLogger().hasErrors()) {
			try {
				Runnable compiled = compiler.compile();
				compiled.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}

			if (wereStuck.size() > 0) {
				MineTweakerAPI.logWarning(Integer.toString(wereStuck.size()) + " modifications were stuck");
				for (IUndoableAction action : wereStuck) {
					MineTweakerAPI.logInfo("Stuck: " + action.describe());
				}
			}
		} else {
			MineTweakerAPI.logInfo("Script has errors, execution halted");
		}
	}

	public byte[] getScriptData()
	{
		return scriptData;
	}
}
