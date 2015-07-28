/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.definition.ParsedInterface;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.ImportableSymbol;
import org.openzen.zencode.symbolic.type.TypeDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicInterface<E extends IPartialExpression<E>>
	extends AbstractSymbolicDefinition<E>
{
	private final ParsedInterface source;
	private final List<IMember<E>> members;
	
	public SymbolicInterface(ParsedInterface source, IModuleScope<E> scope)
	{
		super(source, scope, false, true);
		
		this.source = source;
		members = new ArrayList<>();
	}

	@Override
	public void register(IModuleScope<E> scope)
	{
		scope.putImport(source.getName(),
				new ImportableSymbol<>(new TypeDefinition<>(getTypeVariables(), false, true)),
				source.getPosition());
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
	
	@Override
	public boolean isStruct()
	{
		return false;
	}
}
