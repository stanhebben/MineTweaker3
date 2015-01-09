/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.unit.ParsedExpansion;
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
public class SymbolicExpansion<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	extends AbstractSymbolicDefinition<E, T>
{
	private final ParsedExpansion source;
	private final List<IMember<E, T>> members;
	
	public SymbolicExpansion(ParsedExpansion source, IModuleScope<E, T> scope)
	{
		super(source, scope);
		
		this.source = source;
		this.members = new ArrayList<IMember<E, T>>();
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		for (IParsedMember member : source.getMembers()) {
			member.collectInnerDefinitions(units, scope);
		}
	}

	@Override
	public void compileMembers()
	{
		super.compileMembers();
		
		for (IParsedMember member : source.getMembers()) {
			members.add(member.compile(getScope()));
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
