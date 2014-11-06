/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IScopeClass;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.scope.ScopeClass;
import org.openzen.zencode.util.Strings;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeCompiledClass;

/**
 *
 * @author Stan
 */
public class SymbolicClass implements ISymbolicUnit
{
	private final int modifiers;
	private final String[] className;
	private final ZenType superclass;
	private final List<IMember> members;
	private final SymbolLocal localThis;
	private final IScopeClass scope;
	private final ZenType type;
	
	public SymbolicClass(IScopeModule moduleScope, int modifiers, String[] className, ZenType superclass)
	{
		this.modifiers = modifiers;
		this.className = className;
		this.superclass = superclass;
		members = new ArrayList<IMember>();
		scope = new ScopeClass(moduleScope);
		type = new ZenTypeCompiledClass(scope, this);
		localThis = new SymbolLocal(type, true);
	}
	
	public SymbolicClass(IScopeModule moduleScope, int modifiers, String className, ZenType superclass)
	{
		this(moduleScope, modifiers, Strings.split(className, '.'), superclass);
	}
	
	public IScopeClass getScope()
	{
		return scope;
	}
	
	public SymbolLocal getThis()
	{
		return localThis;
	}
	
	public void addMember(IMember member)
	{
		members.add(member);
	}

	@Override
	public ZenType getType()
	{
		return type;
	}
}
