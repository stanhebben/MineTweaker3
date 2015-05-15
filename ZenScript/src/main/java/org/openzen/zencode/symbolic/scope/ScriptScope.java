/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ScriptScope<E extends IPartialExpression<E>> implements IModuleScope<E>
{
	private final IModuleScope<E> parent;
	private final Map<String, IZenSymbol<E>> imports;
	private final IMethodScope<E> constantScope;
	
	public ScriptScope(IModuleScope<E> parent)
	{
		this.parent = parent;
		this.imports = new HashMap<String, IZenSymbol<E>>();
		this.constantScope = new ConstantScope<E>(this);
	}
	
	@Override
	public IMethodScope<E> getConstantScope()
	{
		return constantScope;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return parent.getAccessScope();
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return parent.getTypeCapture();
	}

	@Override
	public void putImport(String name, IZenSymbol<E> symbol, CodePosition position)
	{
		putValue(name, symbol, position);
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return parent.getEnvironment();
	}

	@Override
	public TypeRegistry<E> getTypeCompiler()
	{
		return parent.getTypeCompiler();
	}

	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return parent.getExpressionCompiler();
	}

	@Override
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		if (imports.containsKey(name))
			return imports.get(name).instance(position, environment);
		else
			return parent.getValue(name, position, environment);
	}

	@Override
	public IZenSymbol<E> getSymbol(String name)
	{
		if (imports.containsKey(name))
			return imports.get(name);
		
		return parent.getSymbol(name);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		imports.put(name, value);
	}

	@Override
	public boolean contains(String name)
	{
		return imports.containsKey(name);
	}

	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return parent.getErrorLogger();
	}

	@Override
	public ZenPackage<E> getRootPackage()
	{
		return parent.getRootPackage();
	}
}
