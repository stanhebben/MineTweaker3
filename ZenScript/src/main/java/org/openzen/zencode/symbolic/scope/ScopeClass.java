/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ScopeClass implements IScopeClass
{
	private final AccessScope accessScope;
	private final IScopeModule module;
	private final Map<String, IZenSymbol> local;

	public ScopeClass(IScopeModule global)
	{
		this.accessScope = AccessScope.createClassScope(global.getAccessScope());
		this.module = global;
		this.local = new HashMap<String, IZenSymbol>();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public TypeRegistry getTypes()
	{
		return module.getTypes();
	}

	@Override
	public IZenCompileEnvironment getEnvironment()
	{
		return module.getEnvironment();
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
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return module.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol value, CodePosition position)
	{
		if (local.containsKey(name))
			error(position, "Value already defined in this scope: " + name);
		else
			local.put(name, value);
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
