/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.definition.ParsedClass;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.ImportableSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolicClass<E extends IPartialExpression<E>>
	extends AbstractSymbolicDefinition<E>
{
	private final ParsedClass source;
	
	private final String className;
	private final IGenericType<E> superclass;
	private final List<IMember<E>> members;
	
	public SymbolicClass(ParsedClass source, IModuleScope<E> moduleScope)
	{
		super(source, moduleScope, false, false);
		
		this.source = source;
		this.className = source.getName();
		
		if (source.getBaseTypes().isEmpty()) {
			superclass = null;
		} else if (source.getBaseTypes().size() > 1) {
			moduleScope.getErrorLogger().errorMultipleSuperclasses(source.getPosition(), source.getName());
			superclass = null;
		} else {
			superclass = source.getBaseTypes().get(0).compile(moduleScope);
		}
		
		members = new ArrayList<IMember<E>>();
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
			IMember<E> compiledMember = member.compile(getScope());
			if (compiledMember != null)
				members.add(compiledMember);
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
		scope.putImport(
				className,
				new ImportableSymbol<>(getDefinition()),
				source.getPosition());
	}
	
	@Override
	public boolean isStruct()
	{
		return false;
	}
}
