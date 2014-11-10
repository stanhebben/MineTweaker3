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
import stanhebben.zenscript.statements.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ScopeMethod implements IScopeMethod
{
	private final IScopeClass scope;
	private final Map<String, IZenSymbol> local;
	private final ZenType returnType;

	public ScopeMethod(IScopeClass environment, ZenType returnType)
	{
		this.scope = environment;
		this.local = new HashMap<String, IZenSymbol>();
		this.returnType = returnType;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return scope.getAccessScope();
	}

	@Override
	public TypeRegistry getTypes()
	{
		return scope.getTypes();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		scope.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		scope.warning(position, message);
	}

	@Override
	public IZenCompileEnvironment getEnvironment()
	{
		return scope.getEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return scope.makeClassName();
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		scope.putClass(name, data);
	}

	@Override
	public boolean containsClass(String name)
	{
		return scope.containsClass(name);
	}

	@Override
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return scope.getValue(name, position, environment);
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
	public Set<String> getClassNames()
	{
		return scope.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return scope.getClass(name);
	}
	
	@Override
	public Map<String, byte[]> getClasses()
	{
		return scope.getClasses();
	}

	@Override
	public Statement getControlStatement(String label)
	{
		return null;
	}

	@Override
	public ZenType getReturnType()
	{
		return returnType;
	}
}
