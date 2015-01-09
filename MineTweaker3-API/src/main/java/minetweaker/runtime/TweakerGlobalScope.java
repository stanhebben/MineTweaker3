/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.java.IJavaScopeGlobal;
import org.openzen.zencode.java.JavaTypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.scope.ScopeConstant;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.util.ClassNameGenerator;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class TweakerGlobalScope implements IJavaScopeGlobal
{
	private final Map<String, byte[]> classes;
	private final ClassNameGenerator generator;
	private final Map<String, IZenSymbol<IJavaExpression, IJavaType>> symbols;

	private final TweakerCompileEnvironment environment;
	private final ScopeConstant<IJavaExpression, IJavaType> constantScope;

	public TweakerGlobalScope(ICodeErrorLogger errors)
	{
		this.classes = new HashMap<String, byte[]>();
		generator = new ClassNameGenerator();
		symbols = new HashMap<String, IZenSymbol<IJavaExpression, IJavaType>>();

		environment = new TweakerCompileEnvironment(this, errors);
		environment.init();
		
		constantScope = new ScopeConstant<IJavaExpression, IJavaType>(this);
	}

	@Override
	public TweakerCompileEnvironment getEnvironment()
	{
		return environment;
	}

	@Override
	public JavaTypeCompiler getTypeCompiler()
	{
		return environment.getTypeCompiler();
	}
	
	@Override
	public IExpressionCompiler<IJavaExpression, IJavaType> getExpressionCompiler()
	{
		return environment.getExpressionCompiler();
	}
	
	@Override
	public IScopeMethod<IJavaExpression, IJavaType> getConstantEnvironment()
	{
		return constantScope;
	}

	@Override
	public String makeClassName()
	{
		return generator.generate();
	}

	@Override
	public boolean containsClass(String name)
	{
		return classes.containsKey(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		classes.put(name, data);
	}

	@Override
	public Map<String, byte[]> getClasses()
	{
		return classes;
	}

	@Override
	public IPartialExpression<IJavaExpression, IJavaType> getValue(String name, CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope)
	{
		if (symbols.containsKey(name))
			return symbols.get(name).instance(position, scope);
		
		return environment.getGlobal(position, scope, name);
	}

	@Override
	public void putValue(String name, IZenSymbol<IJavaExpression, IJavaType> value, CodePosition position)
	{
		if (symbols.containsKey(name))
			error(position, "Value already defined in this scope: " + name);
		else
			symbols.put(name, value);
	}

	@Override
	public boolean hasErrors()
	{
		return environment.getErrorLogger().hasErrors();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		environment.getErrorLogger().error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		environment.getErrorLogger().warning(position, message);
	}

	@Override
	public Set<String> getClassNames()
	{
		return classes.keySet();
	}

	@Override
	public byte[] getClass(String name)
	{
		return classes.get(name);
	}
}
