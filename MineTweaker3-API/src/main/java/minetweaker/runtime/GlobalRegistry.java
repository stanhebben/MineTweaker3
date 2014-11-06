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
import java.util.Set;
import minetweaker.api.IBracketHandler;
import minetweaker.api.IRecipeRemover;
import minetweaker.api.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.runtime.symbol.ITweakerSymbol;
import minetweaker.runtime.symbol.SymbolUtil;
import org.openzen.zencode.IZenCompileEnvironment;
import stanhebben.zenscript.TypeExpansion;
import org.openzen.zencode.util.ClassNameGenerator;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolPackage;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class GlobalRegistry {
	private static final Map<String, ITweakerSymbol> globals = new HashMap<String, ITweakerSymbol>();
	private static final List<IRecipeRemover> removers = new ArrayList<IRecipeRemover>();
	private static final List<IBracketHandler> bracketHandlers = new ArrayList<IBracketHandler>();
	private static final List<Class> annotatedClasses = new ArrayList<Class>();
	
	static {
		registerGlobal("print", SymbolUtil.getStaticMethod(GlobalFunctions.class, "print", String.class));
		registerGlobal("max", SymbolUtil.getStaticMethod(Math.class, "max", int.class, int.class));
		registerGlobal("min", SymbolUtil.getStaticMethod(Math.class, "min", int.class, int.class));
	}
	
	private GlobalRegistry() {}
	
	public static void registerGlobal(String name, ITweakerSymbol symbol) {
		if (globals.containsKey(name)) {
			throw new IllegalArgumentException("symbol already exists: " + name);
		}
		
		globals.put(name, symbol);
	}
	
	public static void registerRemover(IRecipeRemover remover) {
		removers.add(remover);
	}
	
	public static void registerBracketHandler(IBracketHandler handler) {
		bracketHandlers.add(handler);
	}
	
	public static void registerAnnotatedClass(Class cls) {
		annotatedClasses.add(cls);
	}
	
	public static void remove(IIngredient ingredient) {
		for (IRecipeRemover remover : removers) {
			remover.remove(ingredient);
		}
	}
	
	public static IZenSymbol resolveBracket(List<Token> tokens) {
		for (IBracketHandler handler : bracketHandlers) {
			IZenSymbol symbol = handler.resolve(tokens);
			if (symbol != null) {
				return symbol;
			}
		}
		
		return null;
	}
	
	public static IScopeGlobal makeGlobalEnvironment(Map<String, byte[]> classes) {
		return new TweakerGlobalScope(classes);
	}
	
	private static class MyErrorLogger implements ICodeErrorLogger {
		@Override
		public void error(CodePosition position, String message) {
			if (position == null) {
				MineTweakerAPI.logError("system: " + message);
			} else {
				MineTweakerAPI.logError(position + ": " + message);
			}
		}

		@Override
		public void warning(CodePosition position, String message) {
			if (position == null) {
				MineTweakerAPI.logWarning("system: " + message);
			} else {
				MineTweakerAPI.logWarning(position + ": " + message);
			}
		}
	}
	
	private static class MyCompileEnvironment implements IZenCompileEnvironment {
		private final IScopeGlobal scope;
		private final ICodeErrorLogger errors;
		
		private MyCompileEnvironment(IScopeGlobal scope, ICodeErrorLogger errors) {
			this.scope = scope;
			this.errors = errors;
		}
		
		@Override
		public ICodeErrorLogger getErrorLogger() {
			return errors;
		}

		@Override
		public IZenSymbol getGlobal(String name) {
			return null;
		}

		@Override
		public IZenSymbol getDollar(String name) {
			return null;
		}

		@Override
		public IZenSymbol getBracketed(IScopeGlobal environment, List<Token> tokens) {
			return resolveBracket(tokens);
		}

		@Override
		public IAny evalGlobal(String name) {
			// TODO
			return null;
		}

		@Override
		public IAny evalDollar(String name) {
			return null;
		}

		@Override
		public IAny evalBracketed(List<Token> tokens) {
			for (IBracketHandler handler : bracketHandlers) {
				IAny symbol = handler.eval(tokens);
				if (symbol != null) {
					return symbol;
				}
			}

			return null;
		}
	}
	
	private static class TweakerGlobalScope implements IScopeGlobal {
		private final Map<String, byte[]> classes;
		private final Map<String, IZenSymbol> symbols;
		private final ClassNameGenerator generator;
		private final TypeRegistry types;
		
		private final IZenCompileEnvironment environment;
		private final SymbolPackage root = new SymbolPackage("<root>");
		private final ICodeErrorLogger errors = new MyErrorLogger();
		private final Map<String, TypeExpansion> expansions = new HashMap<String, TypeExpansion>();
		
		public TweakerGlobalScope(Map<String, byte[]> classes) {
			this.classes = classes;
			symbols = new HashMap<String, IZenSymbol>();
			for (Map.Entry<String, ITweakerSymbol> entry : globals.entrySet()) {
				symbols.put(entry.getKey(), entry.getValue().convert(this));
			}
			
			generator = new ClassNameGenerator();
			
			types = new TypeRegistry(this);
			environment = new MyCompileEnvironment(this, errors);
			
			// add annotated classes
			for (Class cls : annotatedClasses) {
				
			}
		}
		
		@Override
		public IZenCompileEnvironment getEnvironment() {
			return environment;
		}
		
		@Override
		public TypeRegistry getTypes() {
			return types;
		}

		@Override
		public String makeClassName() {
			return generator.generate();
		}

		@Override
		public boolean containsClass(String name) {
			return classes.containsKey(name);
		}

		@Override
		public void putClass(String name, byte[] data) {
			classes.put(name, data);
		}

		@Override
		public IPartialExpression getValue(String name, CodePosition position, IScopeMethod scope) {
			if (symbols.containsKey(name)) {
				return symbols.get(name).instance(position, scope);
			} else {
				IZenSymbol pkg = root.get(name);
				if (pkg == null) {
					return null;
				} else {
					return pkg.instance(position, scope);
				}
			}
		}

		@Override
		public void putValue(String name, IZenSymbol value, CodePosition position) {
			if (symbols.containsKey(name)) {
				error(position, "Value already defined in this scope: " + name);
			} else {
				symbols.put(name, value);
			}
		}

		@Override
		public void error(CodePosition position, String message) {
			MineTweakerAPI.logError(position.toString() + " > " + message);
		}

		@Override
		public void warning(CodePosition position, String message) {
			MineTweakerAPI.logWarning(position.toString() + " > " + message);
		}

		@Override
		public Set<String> getClassNames() {
			return classes.keySet();
		}

		@Override
		public byte[] getClass(String name) {
			return classes.get(name);
		}
	}
}
