/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.definition.ParsedExpansion;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicExpansion<E extends IPartialExpression<E>>
	extends AbstractSymbolicDefinition<E>
{
	private final ParsedExpansion source;
	private final List<IMember<E>> members;
	
	public SymbolicExpansion(ParsedExpansion source, IModuleScope<E> scope)
	{
		super(source, scope);
		
		this.source = source;
		this.members = new ArrayList<>();
	}
	
	public List<IMember<E>> getMembers()
	{
		return members;
	}

	@Override
	public void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
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

	@Override
	public void register(IModuleScope<E> scope)
	{
		IGenericType<E> type = source.getTargetType().compile(scope);
		for (IMember<E> member : members) {
			type.addMember(member);
		}
	}
	
	@Override
	public boolean isStruct()
	{
		return false;
	}
}
