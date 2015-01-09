/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime;

import java.lang.annotation.Annotation;
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
import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenExpansion;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.java.IJavaScopeGlobal;
import org.openzen.zencode.java.JavaExpressionCompiler;
import org.openzen.zencode.java.JavaNative;
import org.openzen.zencode.java.JavaTypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.AccessType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolPackage;
import org.openzen.zencode.symbolic.symbols.SymbolType;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TweakerCompileEnvironment implements IZenCompileEnvironment<IJavaExpression, IJavaType>
{
	private final TweakerGlobalScope scope;
	private final ICodeErrorLogger errors;
	private final List<IBracketHandler> bracketHandlerInstances = new ArrayList<IBracketHandler>();
	private final JavaTypeCompiler typeCompiler;
	private final JavaExpressionCompiler expressionCompiler;
	private final Map<String, IZenSymbol<IJavaExpression, IJavaType>> globals;
	private final SymbolPackage<IJavaExpression, IJavaType> root = new SymbolPackage<IJavaExpression, IJavaType>("<root>");

	public TweakerCompileEnvironment(TweakerGlobalScope scope, ICodeErrorLogger errors)
	{
		this.scope = scope;
		this.errors = errors;
		typeCompiler = new JavaTypeCompiler(scope);
		expressionCompiler = new JavaExpressionCompiler();
		globals = new HashMap<String, IZenSymbol<IJavaExpression, IJavaType>>();
	}
	
	public void init()
	{
		for (Class<? extends IBracketHandler> bracketHandler : GlobalRegistry.getBracketHandlers())
		{
			try {
				Constructor<? extends IBracketHandler> constructor = bracketHandler.getConstructor(IJavaScopeGlobal.class);
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
		
		
		// add annotated classes
		for (Class<?> cls : GlobalRegistry.getAnnotatedClasses()) {
			try {
				for (Annotation annotation : cls.getAnnotations()) {
					if (annotation instanceof ZenExpansion) {
						String type = ((ZenExpansion) annotation).value();
						TypeExpansion<IJavaExpression, IJavaType> expansion
								= new TypeExpansion<IJavaExpression, IJavaType>(
										scope,
										AccessType.EXPORT,
										ZenType.ACCESS_GLOBAL);
						JavaNative.addExpansion(scope, expansion, cls);
						typeCompiler.addExpansion(type, expansion);
					} else if (annotation instanceof ZenClass)
						root.put(
								((ZenClass) annotation).value(),
								new SymbolType<IJavaExpression, IJavaType>(typeCompiler.getNativeType(null, cls, TypeCapture.<IJavaExpression, IJavaType>empty())),
								errors);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public JavaTypeCompiler getTypeCompiler()
	{
		return typeCompiler;
	}
	
	@Override
	public IExpressionCompiler<IJavaExpression, IJavaType> getExpressionCompiler()
	{
		return expressionCompiler;
	}

	@Override
	public ICodeErrorLogger getErrorLogger()
	{
		return errors;
	}

	@Override
	public IJavaExpression getGlobal(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, String name)
	{
		if (!globals.containsKey(name))
			return null;
		
		return globals.get(name).instance(position, scope).eval();
	}

	@Override
	public IJavaExpression getDollar(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, String name)
	{
		return null;
	}

	@Override
	public IJavaExpression getBracketed(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> environment, List<Token> tokens)
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
}
