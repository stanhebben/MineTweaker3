/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.unit.ParsedStruct;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicStruct<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	extends AbstractSymbolicDefinition<E, T>
{
	private final ParsedStruct source;
	private final List<IMember<E, T>> members;
	
	public SymbolicStruct(int modifiers, IModuleScope<E, T> moduleScope)
	{
		super(modifiers, Collections.<SymbolicAnnotation<E, T>>emptyList(), moduleScope);
		
		source = null;
		this.members = new ArrayList<IMember<E, T>>();
	}
	
	public SymbolicStruct(ParsedStruct source, IModuleScope<E, T> moduleScope)
	{
		super(source, moduleScope);
		
		this.source = source;
		this.members = new ArrayList<IMember<E, T>>();
	}
	
	public void addMember(IMember<E, T> member)
	{
		members.add(member);
	}
	
	public List<IMember<E, T>> getMembers()
	{
		return members;
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		if (source == null)
			return;
		
		for (IParsedMember member : source.getMembers()) {
			member.collectInnerDefinitions(units, scope);
		}
	}

	@Override
	public void compileMembers()
	{
		super.compileMembers();
		
		for (IParsedMember member : source.getMembers()) {
			IMember<E, T> compiled = member.compile(getScope());
			if (compiled != null)
				members.add(compiled);
		}
	}

	@Override
	public void compileMemberContents()
	{
		super.compileMemberContents();
		
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
	}

	@Override
	public void validate()
	{
		super.validate();
		
		for (IMember<E, T> member : members) {
			member.validate();
		}
	}
}
