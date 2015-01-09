/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.member.ParsedImplementation;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ImplementationMember<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IMember<E, T>
{
	private final IDefinitionScope<E, T> unitScope;
	private final ParsedImplementation source;
	private final List<IMember<E, T>> members;
	private final int modifiers;
	
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public ImplementationMember(ParsedImplementation source, IDefinitionScope<E, T> unitScope)
	{
		this.unitScope = unitScope;
		this.source = source;
		members = new ArrayList<IMember<E, T>>();
		for (IParsedMember member : source.getMembers()) {
			IMember<E, T> compiledMember = member.compile(unitScope);
			if (compiledMember != null)
				members.add(compiledMember);
		}
		
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E, T> getUnit()
	{
		return unitScope.getDefinition();
	}

	@Override
	public void completeContents()
	{
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
		
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), unitScope);
	}

	@Override
	public void validate()
	{
		for (IMember<E, T> member : members) {
			member.validate();
		}
		
		// TODO: are all necessary methods implemented?
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
}
