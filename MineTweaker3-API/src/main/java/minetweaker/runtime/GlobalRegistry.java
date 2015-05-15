/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minetweaker.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import minetweaker.api.IBracketHandler;
import minetweaker.api.IRecipeRemover;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.runtime.symbol.ITweakerSymbol;
import minetweaker.runtime.symbol.TweakerSymbols;
import org.openzen.zencode.AbstractErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class GlobalRegistry
{
	private static final Map<String, ITweakerSymbol> globals = new HashMap<String, ITweakerSymbol>();
	private static final List<IRecipeRemover> removers = new ArrayList<IRecipeRemover>();
	private static final List<Class<? extends IBracketHandler>> bracketHandlers = new ArrayList<Class<? extends IBracketHandler>>();
	private static final List<Class<?>> annotatedClasses = new ArrayList<Class<?>>();

	static {
		registerGlobal("print", TweakerSymbols.getStaticMethod(GlobalFunctions.class, "print", String.class));
		registerGlobal("max", TweakerSymbols.getStaticMethod(Math.class, "max", int.class, int.class));
		registerGlobal("min", TweakerSymbols.getStaticMethod(Math.class, "min", int.class, int.class));
	}

	private GlobalRegistry()
	{
	}

	public static void registerGlobal(String name, ITweakerSymbol symbol)
	{
		if (globals.containsKey(name))
			throw new IllegalArgumentException("symbol already exists: " + name);

		globals.put(name, symbol);
	}

	public static void registerRemover(IRecipeRemover remover)
	{
		removers.add(remover);
	}

	public static void registerBracketHandler(Class<? extends IBracketHandler> handler)
	{
		bracketHandlers.add(handler);
	}

	public static void registerAnnotatedClass(Class<?> cls)
	{
		annotatedClasses.add(cls);
	}

	public static void remove(IIngredient ingredient)
	{
		for (IRecipeRemover remover : removers) {
			remover.remove(ingredient);
		}
	}

	public static IZenCompileEnvironment<IJavaExpression> createGlobalEnvironment()
	{
		return new TweakerCompileEnvironment(new MyErrorLogger());
	}
	
	public static Map<String, ITweakerSymbol> getGlobals()
	{
		return globals;
	}
	
	public static List<Class<? extends IBracketHandler>> getBracketHandlers()
	{
		return bracketHandlers;
	}
	
	public static List<Class<?>> getAnnotatedClasses()
	{
		return annotatedClasses;
	}

	private static class MyErrorLogger extends AbstractErrorLogger<IJavaExpression>
	{
		private boolean hasErrors = false;
		
		@Override
		public boolean hasErrors()
		{
			return hasErrors;
		}
		
		@Override
		public void error(CodePosition position, String message)
		{
			hasErrors = true;
			
			if (position == null)
				MineTweakerAPI.logError("system: " + message);
			else
				MineTweakerAPI.logError(position + ": " + message);
		}

		@Override
		public void warning(CodePosition position, String message)
		{
			if (position == null)
				MineTweakerAPI.logWarning("system: " + message);
			else
				MineTweakerAPI.logWarning(position + ": " + message);
		}
	}
}
