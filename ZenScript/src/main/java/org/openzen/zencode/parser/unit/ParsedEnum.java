/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.unit;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.expression.ParsedCallArguments;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.symbolic.unit.SymbolicEnum;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedEnum implements IParsedDefinition
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final List<Value> values;
	private final List<IParsedMember> members;
	
	public ParsedEnum(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			List<Value> values,
			List<IParsedMember> members)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.values = values;
		this.members = members;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	@Override
	public List<ParsedAnnotation> getAnnotations()
	{
		return annotations;
	}

	@Override
	public List<IParsedModifier> getModifiers()
	{
		return modifiers;
	}
	
	@Override
	public List<ParsedGenericParameter> getGenericParameters()
	{
		return Collections.<ParsedGenericParameter>emptyList();
	}

	public String getName()
	{
		return name;
	}

	public List<Value> getValues()
	{
		return values;
	}

	public List<IParsedMember> getMembers()
	{
		return members;
	}

	@Override
	public <E extends IPartialExpression<E>> ISymbolicDefinition<E> compile(IModuleScope<E> scope)
	{
		return new SymbolicEnum<E>(this, scope);
	}
	
	public static class Value
	{
		public final CodePosition position;
		public final String name;
		public final ParsedCallArguments arguments;
		
		public Value(CodePosition position, String name, ParsedCallArguments arguments)
		{
			this.position = position;
			this.name = name;
			this.arguments = arguments;
		}
	}
}
