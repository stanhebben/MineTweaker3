/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.unit.ParsedClass;
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
public class SymbolicClass<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	extends AbstractSymbolicDefinition<E, T>
{
	private final ParsedClass source;
	
	private final String className;
	private final T superclass;
	private final List<IMember<E, T>> members;
	
	public SymbolicClass(ParsedClass source, IModuleScope<E, T> moduleScope)
	{
		super(source, moduleScope);
		
		this.source = source;
		this.className = source.getName();
		
		if (source.getExtendsTypes().isEmpty()) {
			superclass = null;
		} else if (source.getExtendsTypes().size() > 1) {
			moduleScope.getErrorLogger().errorMultipleSuperclasses(source.getPosition(), source.getName());
			superclass = null;
		} else {
			superclass = source.getExtendsTypes().get(0).compile(moduleScope);
		}
		
		members = new ArrayList<IMember<E, T>>();
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
			IMember<E, T> compiledMember = member.compile(getScope());
			if (compiledMember != null)
				members.add(compiledMember);
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
