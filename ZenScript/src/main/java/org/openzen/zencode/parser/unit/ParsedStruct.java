/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.unit;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.symbolic.unit.SymbolicStruct;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedStruct implements IParsedDefinition
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final List<IParsedMember> members;
	
	public ParsedStruct(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			List<IParsedMember> members)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
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

	public String getName()
	{
		return name;
	}

	public List<IParsedMember> getMembers()
	{
		return members;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> ISymbolicDefinition<E, T> compile(IModuleScope<E, T> scope)
	{
		return new SymbolicStruct<E, T>(this, scope);
	}

	@Override
	public List<ParsedGenericParameter> getGenericParameters()
	{
		return Collections.emptyList();
	}
}
