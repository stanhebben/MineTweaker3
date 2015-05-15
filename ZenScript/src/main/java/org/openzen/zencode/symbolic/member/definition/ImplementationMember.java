/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.member.ParsedImplementation;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IImplementationMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ImplementationMember<E extends IPartialExpression<E>> implements IImplementationMember<E>
{
	private final IDefinitionScope<E> unitScope;
	private final ParsedImplementation source;
	private final List<IMember<E>> members;
	private final int modifiers;
	private final IGenericType<E> type;
	
	private List<SymbolicAnnotation<E>> annotations;
	
	public ImplementationMember(ParsedImplementation source, IDefinitionScope<E> unitScope)
	{
		this.unitScope = unitScope;
		this.source = source;
		members = new ArrayList<IMember<E>>();
		for (IParsedMember member : source.getMembers()) {
			IMember<E> compiledMember = member.compile(unitScope);
			if (compiledMember != null)
				members.add(compiledMember);
		}
		
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		type = source.getType().compile(unitScope);
	}
	
	public IGenericType<E> getType()
	{
		return type;
	}

	@Override
	public void completeContents()
	{
		for (IMember<E> member : members) {
			member.completeContents();
		}
		
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), unitScope);
	}

	@Override
	public void validate()
	{
		for (IMember<E> member : members) {
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
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return annotations;
	}

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onImplementation(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return unitScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
}
