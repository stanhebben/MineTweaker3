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
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class SymbolicModule
{
	private final IScopeModule scope;
	private final List<SymbolicFunction> scripts = new ArrayList<SymbolicFunction>();
	private final List<ISymbolicUnit> units = new ArrayList<ISymbolicUnit>();
	
	public SymbolicModule(IScopeGlobal scope)
	{
		this.scope = new ScopeModule(scope);
	}
	
	public IScopeModule getScope()
	{
		return scope;
	}
	
	public void addUnit(ISymbolicUnit unit)
	{
		units.add(unit);
	}
	
	public void addScript(SymbolicFunction script)
	{
		scripts.add(script);
	}
	
	public void compile(MethodOutput mainScript)
	{
		for (ISymbolicUnit unit : units) {
			unit.compile();
		}
		
		for (SymbolicFunction script : scripts) {
			script.compile();
			mainScript.invokeStatic(script.getClassName(), "call", "()V");
		}
	}
}
