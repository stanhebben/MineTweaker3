/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IGlobalScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.ModuleScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicModule<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final IModuleScope<E, T> scope;
	private final List<ScriptBlock<E, T>> scripts = new ArrayList<ScriptBlock<E, T>>();
	private final List<ISymbolicDefinition<E, T>> definitions = new ArrayList<ISymbolicDefinition<E, T>>();
	
	public SymbolicModule(IGlobalScope<E, T> scope)
	{
		this.scope = new ModuleScope<E, T>(scope);
	}
	
	public IModuleScope<E, T> getScope()
	{
		return scope;
	}
	
	public void addUnit(ISymbolicDefinition<E, T> unit)
	{
		definitions.add(unit);
	}
	
	public void addScript(ScriptBlock<E, T> script)
	{
		scripts.add(script);
	}

	public List<ScriptBlock<E, T>> getScripts()
	{
		return scripts;
	}

	public List<ISymbolicDefinition<E, T>> getDefinitions()
	{
		return definitions;
	}
	
	public void compileDefinitions()
	{
		List<ISymbolicDefinition<E, T>> innerDefinitions = new ArrayList<ISymbolicDefinition<E, T>>();
		for (ISymbolicDefinition<E, T> definition : definitions) {
			definition.collectInnerDefinitions(innerDefinitions, scope);
		}
		definitions.addAll(innerDefinitions);
	}
	
	public void compileMembers()
	{
		for (ISymbolicDefinition<E, T> definition : definitions) {
			definition.compileMembers();
		}
	}
	
	public void compileMemberContents()
	{
		for (ISymbolicDefinition<E, T> definition : definitions) {
			definition.compileMemberContents();
		}
	}
	
	public void validate()
	{
		for (ISymbolicDefinition<E, T> definition : definitions) {
			definition.validate();
		}
	}
}
