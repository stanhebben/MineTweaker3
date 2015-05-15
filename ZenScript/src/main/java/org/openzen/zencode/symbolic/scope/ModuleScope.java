/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class ModuleScope<E extends IPartialExpression<E>>
	implements IModuleScope<E>
{
	private final IZenCompileEnvironment<E> environment;
	private final IExpressionCompiler<E> expressionCompiler;
	private final TypeRegistry<E> typeRegistry;
	private final Map<String, IZenSymbol<E>> imports;
	private final AccessScope accessScope;
	private final IMethodScope<E> constantScope;

	public ModuleScope(
			IZenCompileEnvironment<E> environment,
			IExpressionCompiler<E> compiler,
			TypeRegistry<E> typeRegistry)
	{
		this.environment = environment;
		this.expressionCompiler = compiler;
		this.typeRegistry = typeRegistry;
		imports = new HashMap<String, IZenSymbol<E>>();
		accessScope = AccessScope.createModuleScope();
		constantScope = new ConstantScope<E>(this);
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return environment;
	}
	
	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return expressionCompiler;
	}
	
	@Override
	public IMethodScope<E> getConstantScope()
	{
		return constantScope;
	}

	@Override
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		if (imports.containsKey(name)) {
			IZenSymbol<E> imprt = imports.get(name);
			if (imprt == null)
				throw new RuntimeException("How could this happen?");
			return imprt.instance(position, environment);
		} else
			return environment.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");

		if (imports.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			imports.put(name, value);
	}

	@Override
	public TypeRegistry<E> getTypeCompiler()
	{
		return typeRegistry;
	}

	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return environment.getErrorLogger();
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return TypeCapture.empty();
	}

	@Override
	public ZenPackage<E> getRootPackage()
	{
		return environment.getRootPackage();
	}

	@Override
	public IZenSymbol<E> getSymbol(String name)
	{
		if (imports.containsKey(name))
			return imports.get(name);
		
		return environment.getGlobal(name);
	}

	@Override
	public boolean contains(String name)
	{
		return imports.containsKey(name) || environment.getGlobal(name) != null;
	}

	@Override
	public void putImport(String name, IZenSymbol<E> symbol, CodePosition position)
	{
		putValue(name, symbol, position);
	}
}
