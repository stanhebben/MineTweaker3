/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.type.ParameterType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class DefinitionScope<E extends IPartialExpression<E>> implements IDefinitionScope<E>
{
	private final AccessScope accessScope;
	private final IModuleScope<E> module;
	private final ISymbolicDefinition<E> unit;
	private final Map<String, IZenSymbol<E>> local;
	private final TypeCapture<E> typeCapture;
	private final IMethodScope<E> constantScope;
	private final TypeInstance<E> type;
	
	public DefinitionScope(IModuleScope<E> global, ISymbolicDefinition<E> unit, TypeInstance<E> type)
	{
		this.accessScope = AccessScope.createClassScope(global.getAccessScope());
		this.module = global;
		this.local = new HashMap<>();
		this.unit = unit;
		this.typeCapture = new TypeCapture<>(null);
		constantScope = new ConstantScope<>(this);
		this.type = type;
		
		for (ITypeVariable<E> typeVariable : unit.getTypeVariables()) {
			typeCapture.put(typeVariable, new TypeInstance<>(new ParameterType<>(this, typeVariable), Collections.emptyList(), false));
		}
	}
	
	@Override
	public ISymbolicDefinition<E> getDefinition()
	{
		return unit;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public TypeRegistry<E> getTypeCompiler()
	{
		return module.getTypeCompiler();
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return module.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return module.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E> getConstantScope()
	{
		return constantScope;
	}

	@Override
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return module.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		if (local.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			local.put(name, value);
	}

	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return module.getErrorLogger();
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return typeCapture;
	}

	@Override
	public ZenPackage<E> getRootPackage()
	{
		return module.getRootPackage();
	}

	@Override
	public IZenSymbol<E> getSymbol(String name)
	{
		if (local.containsKey(name))
			return local.get(name);
		
		return module.getSymbol(name);
	}

	@Override
	public boolean contains(String name)
	{
		return local.containsKey(name) || module.contains(name);
	}

	@Override
	public void putImport(String name, IZenSymbol<E> symbol, CodePosition position)
	{
		putValue(name, symbol, position);
	}

	@Override
	public TypeInstance<E> getSelfType()
	{
		return type;
	}
}
