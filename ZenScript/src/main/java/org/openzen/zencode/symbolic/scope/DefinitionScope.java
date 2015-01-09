/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class DefinitionScope<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IDefinitionScope<E, T>
{
	private final AccessScope accessScope;
	private final IModuleScope<E, T> module;
	private final ISymbolicDefinition<E, T> unit;
	private final Map<String, IZenSymbol<E, T>> local;
	private final TypeCapture<E, T> typeCapture;
	
	public DefinitionScope(IModuleScope<E, T> global, ISymbolicDefinition<E, T> unit)
	{
		this.accessScope = AccessScope.createClassScope(global.getAccessScope());
		this.module = global;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
		this.unit = unit;
		this.typeCapture = new TypeCapture<E, T>(null);
		
		for (ITypeVariable<E, T> typeVariable : unit.getTypeVariables()) {
			typeCapture.put(typeVariable, global.getTypeCompiler().getGeneric(typeVariable));
		}
	}
	
	@Override
	public ISymbolicDefinition<E, T> getDefinition()
	{
		return unit;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public ITypeCompiler<E, T> getTypeCompiler()
	{
		return module.getTypeCompiler();
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return module.getEnvironment();
	}
	
	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return module.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E, T> getConstantEnvironment()
	{
		return module.getConstantEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return module.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return module.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		module.putClass(name, data);
	}
	
	@Override
	public Map<String, byte[]> getClasses()
	{
		return module.getClasses();
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return module.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
	{
		if (local.containsKey(name))
			getErrorLogger().errorSymbolNameAlreadyExists(position, name);
		else
			local.put(name, value);
	}

	@Override
	public Set<String> getClassNames()
	{
		return module.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return module.getClass(name);
	}

	@Override
	public ICodeErrorLogger<E, T> getErrorLogger()
	{
		return module.getErrorLogger();
	}

	@Override
	public TypeCapture<E, T> getTypeCapture()
	{
		return typeCapture;
	}
}
