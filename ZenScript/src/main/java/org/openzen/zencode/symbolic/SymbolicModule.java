/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.scope.ScopeModule;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicModule<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final IScopeModule<E, T> scope;
	private final List<SymbolicFunction<E, T>> scripts = new ArrayList<SymbolicFunction<E, T>>();
	private final List<ISymbolicUnit<E, T>> units = new ArrayList<ISymbolicUnit<E, T>>();
	
	public SymbolicModule(IScopeGlobal<E, T> scope)
	{
		this.scope = new ScopeModule<E, T>(scope);
	}
	
	public IScopeModule<E, T> getScope()
	{
		return scope;
	}
	
	public void addUnit(ISymbolicUnit<E, T> unit)
	{
		units.add(unit);
	}
	
	public void addScript(SymbolicFunction<E, T> script)
	{
		scripts.add(script);
	}
	
	/*public void compile(MethodOutput mainScript)
	{
		for (ISymbolicUnit unit : units) {
			unit.compile();
		}
		
		for (SymbolicFunction script : scripts) {
			script.compile();
			mainScript.invokeStatic(script.getClassName(), "call", "()V");
		}
	}*/
}
