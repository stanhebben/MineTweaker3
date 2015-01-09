/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.unit;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.elements.ParsedGenericParameter;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.symbolic.unit.SymbolicClass;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedClass implements IParsedDefinition
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final List<ParsedGenericParameter> genericParameters;
	private final List<IParsedType> extendsTypes;
	private final List<IParsedMember> members;
	
	public ParsedClass(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			List<ParsedGenericParameter> genericParameters,
			List<IParsedType> extendsTypes,
			List<IParsedMember> members)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.genericParameters = genericParameters;
		this.extendsTypes = extendsTypes;
		this.members = members;
	}

	public CodePosition getPosition()
	{
		return position;
	}

	public List<ParsedAnnotation> getAnnotations()
	{
		return annotations;
	}

	public List<IParsedModifier> getModifiers()
	{
		return modifiers;
	}

	public String getName()
	{
		return name;
	}

	public List<ParsedGenericParameter> getGenericParameters()
	{
		return genericParameters;
	}

	public List<IParsedType> getExtendsTypes()
	{
		return extendsTypes;
	}

	public List<IParsedMember> getMembers()
	{
		return members;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> ISymbolicDefinition<E, T> compile(IModuleScope<E, T> scope)
	{
		return new SymbolicClass<E, T>(this, scope);
	}
}
