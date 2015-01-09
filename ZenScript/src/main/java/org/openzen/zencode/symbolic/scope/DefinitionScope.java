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
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
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
	
	public DefinitionScope(IModuleScope<E> global, ISymbolicDefinition<E> unit)
	{
		this.accessScope = AccessScope.createClassScope(global.getAccessScope());
		this.module = global;
		this.local = new HashMap<String, IZenSymbol<E>>();
		this.unit = unit;
		this.typeCapture = new TypeCapture<E>(null);
		
		for (ITypeVariable<E> typeVariable : unit.getTypeVariables()) {
			typeCapture.put(typeVariable, global.getTypeCompiler().getGeneric(typeVariable));
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
	public ITypeCompiler<E> getTypeCompiler()
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
	public IMethodScope<E> getConstantEnvironment()
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
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return module.getErrorLogger();
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return typeCapture;
	}
}
