/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.definition;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.member.IParsedMember;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.definition.SymbolicClass;
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
	private final List<IParsedType> baseTypes;
	private final List<IParsedMember> members;
	
	public ParsedClass(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			List<ParsedGenericParameter> genericParameters,
			List<IParsedType> baseTypes,
			List<IParsedMember> members)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.genericParameters = genericParameters;
		this.baseTypes = baseTypes;
		this.members = members;
	}

	public String getName()
	{
		return name;
	}

	public List<IParsedType> getBaseTypes()
	{
		return baseTypes;
	}

	public List<IParsedMember> getMembers()
	{
		return members;
	}
	
	// ########################################
	// ### IParsedDefinition implementation ###
	// ########################################

	@Override
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
		return genericParameters;
	}

	@Override
	public <E extends IPartialExpression<E>> ISymbolicDefinition<E> compile(IModuleScope<E> scope)
	{
		return new SymbolicClass<E>(this, scope);
	}
}
