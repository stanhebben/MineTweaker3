/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.ModuleScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicModule<E extends IPartialExpression<E>>
{
	private final IModuleScope<E> scope;
	private final List<ScriptBlock<E>> scripts = new ArrayList<ScriptBlock<E>>();
	private final List<ISymbolicDefinition<E>> definitions = new ArrayList<ISymbolicDefinition<E>>();
	
	public SymbolicModule(
			IZenCompileEnvironment<E> environment,
			IExpressionCompiler<E> expressionCompiler,
			TypeRegistry<E> typeRegistry)
	{
		this.scope = new ModuleScope<E>(environment, expressionCompiler, typeRegistry);
	}
	
	public IModuleScope<E> getScope()
	{
		return scope;
	}
	
	public void addUnit(ISymbolicDefinition<E> unit)
	{
		definitions.add(unit);
		unit.register(scope);
	}
	
	public void addScript(ScriptBlock<E> script)
	{
		scripts.add(script);
	}

	public List<ScriptBlock<E>> getScripts()
	{
		return scripts;
	}

	public List<ISymbolicDefinition<E>> getDefinitions()
	{
		return definitions;
	}
	
	public void compileDefinitions()
	{
		List<ISymbolicDefinition<E>> innerDefinitions = new ArrayList<ISymbolicDefinition<E>>();
		for (ISymbolicDefinition<E> definition : definitions) {
			definition.collectInnerDefinitions(innerDefinitions, scope);
		}
		definitions.addAll(innerDefinitions);
	}
	
	public void compileMembers()
	{
		for (ISymbolicDefinition<E> definition : definitions) {
			definition.compileMembers();
		}
	}
	
	public void compileMemberContents()
	{
		for (ISymbolicDefinition<E> definition : definitions) {
			definition.compileMemberContents();
		}
	}
	
	public void validate()
	{
		for (ISymbolicDefinition<E> definition : definitions) {
			definition.validate();
		}
	}
}
