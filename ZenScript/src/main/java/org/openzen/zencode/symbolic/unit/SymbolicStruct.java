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

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicStruct<E extends IPartialExpression<E>>
	extends AbstractSymbolicDefinition<E>
{
	private final ParsedStruct source;
	private final List<IMember<E>> members;
	
	public SymbolicStruct(int modifiers, IModuleScope<E> moduleScope)
	{
		super(modifiers, Collections.<SymbolicAnnotation<E>>emptyList(), moduleScope);
		
		source = null;
		this.members = new ArrayList<IMember<E>>();
	}
	
	public SymbolicStruct(ParsedStruct source, IModuleScope<E> moduleScope)
	{
		super(source, moduleScope);
		
		this.source = source;
		this.members = new ArrayList<IMember<E>>();
	}
	
	public void addMember(IMember<E> member)
	{
		members.add(member);
	}
	
	public List<IMember<E>> getMembers()
	{
		return members;
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
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
			IMember<E> compiled = member.compile(getScope());
			if (compiled != null)
				members.add(compiled);
		}
	}

	@Override
	public void compileMemberContents()
	{
		super.compileMemberContents();
		
		for (IMember<E> member : members) {
			member.completeContents();
		}
	}

	@Override
	public void validate()
	{
		super.validate();
		
		for (IMember<E> member : members) {
			member.validate();
		}
	}
}
