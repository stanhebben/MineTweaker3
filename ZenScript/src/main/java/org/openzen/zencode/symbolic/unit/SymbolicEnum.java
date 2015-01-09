/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.unit.ParsedEnum;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.IMethod;
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
public class SymbolicEnum<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements ISymbolicDefinition<E, T>
{
	private final ParsedEnum source;
	private final int modifiers;
	private final String name;
	private final List<EnumValue> values;
	private final IDefinitionScope<E, T> scope;
	private final List<IMember<E, T>> members;
	
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public SymbolicEnum(ParsedEnum source, IModuleScope<E, T> moduleScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), moduleScope.getErrorLogger());
		this.name = source.getName();
		this.values = new ArrayList<EnumValue>();
		this.scope = new DefinitionScope<E, T>(moduleScope, this);
		this.members = new ArrayList<IMember<E, T>>();
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
		
		for (ParsedEnum.Value value : source.getValues()) {
			values.add(new EnumValue(value));
		}
	}

	@Override
	public void compileMemberContents()
	{
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), scope);
		
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
		
		for (EnumValue value : values) {
			value.completeContents();
		}
	}
	
	public final class EnumValue
	{
		private final ParsedEnum.Value source;
		
		private final String name;
		
		private IMethod<E, T> constructor;
		private List<E> arguments;
		
		public EnumValue(ParsedEnum.Value source)
		{
			this.name = source.name;
			this.source = source;
		}
		
		private void completeContents()
		{
			// TODO: get constructors and compile values
		}
	}
}
