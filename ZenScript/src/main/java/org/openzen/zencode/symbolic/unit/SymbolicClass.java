/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IScopeClass;
import org.openzen.zencode.symbolic.scope.IScopeModule;
import org.openzen.zencode.symbolic.scope.ScopeClass;
import org.openzen.zencode.util.Strings;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicClass<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements ISymbolicUnit<E, T>
{
	private final int modifiers;
	private final String[] className;
	private final T superclass;
	private final List<IMember<E, T>> members;
	private final IScopeClass<E, T> scope;
	
	public SymbolicClass(IScopeModule<E, T> moduleScope, int modifiers, String[] className, T superclass)
	{
		this.modifiers = modifiers;
		this.className = className;
		this.superclass = superclass;
		members = new ArrayList<IMember<E, T>>();
		scope = new ScopeClass<E, T>(moduleScope);
	}
	
	public SymbolicClass(IScopeModule<E, T> moduleScope, int modifiers, String className, T superclass)
	{
		this(moduleScope, modifiers, Strings.split(className, '.'), superclass);
	}
	
	public IScopeClass<E, T> getScope()
	{
		return scope;
	}
	
	/*public SymbolLocal<E, T> getThis()
	{
		//return localThis;
	}*/
	
	public void addMember(IMember<E, T> member)
	{
		members.add(member);
	}
}
