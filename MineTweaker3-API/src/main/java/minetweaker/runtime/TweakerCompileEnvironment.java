/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import minetweaker.api.IBracketHandler;
import minetweaker.runtime.symbol.ITweakerSymbol;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.java.JavaCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.ModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TweakerCompileEnvironment implements IZenCompileEnvironment<IJavaExpression>
{
	private final ICodeErrorLogger<IJavaExpression> errors;
	private final List<IBracketHandler> bracketHandlerInstances = new ArrayList<IBracketHandler>();
	private final Map<String, IZenSymbol<IJavaExpression>> globals;
	private final ZenPackage<IJavaExpression> root = ZenPackage.makeRootPackage();

	public TweakerCompileEnvironment(ICodeErrorLogger<IJavaExpression> errors)
	{
		this.errors = errors;
		globals = new HashMap<String, IZenSymbol<IJavaExpression>>();
	}
	
	public void init(JavaCompiler compiler)
	{
		IModuleScope<IJavaExpression> scope = new ModuleScope<IJavaExpression>(this, compiler);
		
		// add annotated classes
		for (Class<?> cls : GlobalRegistry.getAnnotatedClasses()) {
			compiler.addAnnotatedClass(cls);
		}
		
		for (Class<? extends IBracketHandler> bracketHandler : GlobalRegistry.getBracketHandlers())
		{
			try {
				Constructor<? extends IBracketHandler> constructor = bracketHandler.getConstructor(IModuleScope.class);
				bracketHandlerInstances.add(constructor.newInstance(scope));
			} catch (NoSuchMethodException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SecurityException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InstantiationException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalArgumentException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InvocationTargetException ex) {
				Logger.getLogger(TweakerCompileEnvironment.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		for (Map.Entry<String, ITweakerSymbol> global : GlobalRegistry.getGlobals().entrySet()) {
			globals.put(global.getKey(), global.getValue().convert(scope));
		}
	}

	@Override
	public ICodeErrorLogger<IJavaExpression> getErrorLogger()
	{
		return errors;
	}

	@Override
	public IZenSymbol<IJavaExpression> getGlobal(String name)
	{
		if (!globals.containsKey(name))
			return null;
		
		return globals.get(name);
	}

	@Override
	public IJavaExpression getDollar(CodePosition position, IMethodScope<IJavaExpression> scope, String name)
	{
		return null;
	}

	@Override
	public IJavaExpression getBracketed(CodePosition position, IMethodScope<IJavaExpression> environment, List<Token> tokens)
	{
		for (IBracketHandler handler : bracketHandlerInstances) {
			IJavaExpression symbol = handler.resolve(position, environment, tokens);
			if (symbol != null)
				return symbol;
		}

		return null;
	}

	@Override
	public IAny evalGlobal(String name)
	{
		if (!GlobalRegistry.getGlobals().containsKey(name))
			return null;
		
		return GlobalRegistry.getGlobals().get(name).eval();
	}

	@Override
	public IAny evalDollar(String name)
	{
		return null;
	}

	@Override
	public IAny evalBracketed(List<Token> tokens)
	{
		for (IBracketHandler handler : bracketHandlerInstances) {
			IAny symbol = handler.eval(tokens);
			if (symbol != null)
				return symbol;
		}

		return null;
	}

	@Override
	public ZenPackage<IJavaExpression> getRootPackage()
	{
		return root;
	}
}
