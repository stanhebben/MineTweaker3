/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.AccessScope;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ScopeModule implements IScopeModule
{
	private final IScopeGlobal parent;
	private final Map<String, IZenSymbol> imports;
	private final AccessScope accessScope;

	public ScopeModule(IScopeGlobal parent)
	{
		this.parent = parent;
		imports = new HashMap<String, IZenSymbol>();
		accessScope = AccessScope.createModuleScope();
	}

	@Override
	public AccessScope getAccessScope()
	{
		return accessScope;
	}

	@Override
	public IZenCompileEnvironment getEnvironment()
	{
		return parent.getEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return parent.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return parent.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		parent.putClass(name, data);
	}

	@Override
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment)
	{
		if (imports.containsKey(name)) {
			IZenSymbol imprt = imports.get(name);
			if (imprt == null)
				throw new RuntimeException("How could this happen?");
			return imprt.instance(position, environment);
		} else
			return parent.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol value, CodePosition position)
	{
		if (value == null)
			throw new IllegalArgumentException("value cannot be null");

		if (imports.containsKey(name))
			error(position, "Value already defined in this scope: " + name);
		else
			imports.put(name, value);
	}

	@Override
	public TypeRegistry getTypes()
	{
		return parent.getTypes();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		parent.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		parent.warning(position, message);
	}

	@Override
	public Set<String> getClassNames()
	{
		return parent.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return parent.getClass(name);
	}
}
