/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ScopeClass<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IScopeClass<E, T>
{
	private final AccessScope accessScope;
	private final IScopeModule<E, T> module;
	private final Map<String, IZenSymbol<E, T>> local;

	public ScopeClass(IScopeModule<E, T> global)
	{
		this.accessScope = AccessScope.createClassScope(global.getAccessScope());
		this.module = global;
		this.local = new HashMap<String, IZenSymbol<E, T>>();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public ITypeCompiler<E, T> getTypes()
	{
		return module.getTypes();
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
	public IScopeMethod<E, T> getConstantEnvironment()
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
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IScopeMethod<E, T> environment)
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
			error(position, "Value already defined in this scope: " + name);
		else
			local.put(name, value);
	}
	
	@Override
	public boolean hasErrors()
	{
		return module.hasErrors();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		module.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		module.warning(position, message);
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
}
