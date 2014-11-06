/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.symbolic.AccessScope;
import stanhebben.zenscript.statements.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ScopeStatementBlock implements IScopeMethod
{
	private final IScopeMethod outer;
	private final Map<String, IZenSymbol> local;
	private final Map<SymbolLocal, Integer> locals;

	private final Statement controlStatement;
	private final List<String> labels;

	public ScopeStatementBlock(IScopeMethod outer)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol>();
		this.locals = new HashMap<SymbolLocal, Integer>();
		this.controlStatement = null;
		this.labels = null;
	}

	public ScopeStatementBlock(IScopeMethod outer, Statement controlStatement, String label)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol>();
		this.locals = new HashMap<SymbolLocal, Integer>();
		this.controlStatement = controlStatement;
		this.labels = label == null ? null : Collections.singletonList(label);
	}

	public ScopeStatementBlock(IScopeMethod outer, Statement controlStatement, List<String> labels)
	{
		this.outer = outer;
		this.local = new HashMap<String, IZenSymbol>();
		this.locals = new HashMap<SymbolLocal, Integer>();
		this.controlStatement = controlStatement;
		this.labels = labels;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return outer.getAccessScope();
	}

	@Override
	public TypeRegistry getTypes()
	{
		return outer.getTypes();
	}

	@Override
	public IZenCompileEnvironment getEnvironment()
	{
		return outer.getEnvironment();
	}

	@Override
	public String makeClassName()
	{
		return outer.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return outer.containsClass(name);
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		outer.putClass(name, data);
	}

	@Override
	public IPartialExpression getValue(String name, CodePosition position, IScopeMethod environment)
	{
		if (local.containsKey(name))
			return local.get(name).instance(position, environment);
		else
			return outer.getValue(name, position, environment);
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
		outer.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		outer.warning(position, message);
	}

	@Override
	public Set<String> getClassNames()
	{
		return outer.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return outer.getClass(name);
	}

	@Override
	public Statement getControlStatement(String label)
	{
		if (label == null && controlStatement != null)
			return controlStatement;
		else if (this.labels != null && this.labels.contains(label))
			return controlStatement;
		else
			return outer.getControlStatement(label);
	}

	@Override
	public ZenType getReturnType()
	{
		return outer.getReturnType();
	}
}
