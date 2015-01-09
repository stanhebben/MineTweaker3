/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.unit.ParsedStruct;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.DefinitionScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicStruct<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements ISymbolicDefinition<E, T>
{
	private final ParsedStruct source;
	private final List<IMember<E, T>> members;
	private final IDefinitionScope<E, T> scope;
	private final int modifiers;
	
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public SymbolicStruct(int modifiers, IModuleScope<E, T> moduleScope)
	{
		source = null;
		this.members = new ArrayList<IMember<E, T>>();
		this.scope = new DefinitionScope<E, T>(moduleScope, this);
		this.modifiers = modifiers;
	}
	
	public SymbolicStruct(ParsedStruct source, IModuleScope<E, T> moduleScope)
	{
		this.source = source;
		this.members = new ArrayList<IMember<E, T>>();
		this.scope = new DefinitionScope<E, T>(moduleScope, this);
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), moduleScope.getErrorLogger());
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
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public List<SymbolicAnnotation<E, T>> getAnnotations()
	{
		return annotations;
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
		for (IParsedMember member : source.getMembers()) {
			IMember<E, T> compiled = member.compile(scope);
			if (compiled != null)
				members.add(compiled);
		}
	}

	@Override
	public void compileMemberContents()
	{
		if (source != null)
			annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), scope);
		
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
	}
}
