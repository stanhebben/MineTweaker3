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
public class SymbolicClass<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements ISymbolicDefinition<E, T>
{
	private final ParsedClass source;
	
	private final int modifiers;
	private final String className;
	private final T superclass;
	private final List<IMember<E, T>> members;
	private final IDefinitionScope<E, T> scope;
	
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public SymbolicClass(ParsedClass source, IModuleScope<E, T> moduleScope)
	{
		this.source = source;
		
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), moduleScope.getErrorLogger());
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
		scope = new DefinitionScope<E, T>(moduleScope, this);
	}
	
	public IDefinitionScope<E, T> getScope()
	{
		return scope;
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
		for (IParsedMember member : source.getMembers()) {
			IMember<E, T> compiledMember = member.compile(scope);
			if (compiledMember != null)
				members.add(compiledMember);
		}
	}

	@Override
	public void compileMemberContents()
	{
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
		
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), scope);
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
