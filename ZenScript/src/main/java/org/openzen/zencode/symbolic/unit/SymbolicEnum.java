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
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolicEnum<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	extends AbstractSymbolicDefinition<E, T>
{
	private final ParsedEnum source;
	private final String name;
	private final List<EnumValue> values;
	private final List<IMember<E, T>> members;
	
	public SymbolicEnum(ParsedEnum source, IModuleScope<E, T> moduleScope)
	{
		super(source, moduleScope);
		
		this.source = source;
		this.name = source.getName();
		this.values = new ArrayList<EnumValue>();
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
			IMember<E, T> compiledMember = member.compile(getScope());
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
		super.compileMemberContents();
		
		for (IMember<E, T> member : members) {
			member.completeContents();
		}
		
		for (EnumValue value : values) {
			value.completeContents();
		}
	}

	@Override
	public void validate()
	{
		super.validate();
		
		for (IMember<E, T> member : members) {
			member.validate();
		}
		
		for (EnumValue value : values) {
			value.validate();
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
		
		private void validate()
		{
			for (E argument : arguments) {
				argument.validate();
			}
			
			constructor.validateCall(source.position, getScope().getConstantEnvironment(), arguments);
		}
	}
}
