/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.member.SetterMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedSetMember implements IParsedMember
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final IParsedType asType;
	private final ParsedStatement content;
	
	public ParsedSetMember(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			IParsedType asType,
			ParsedStatement content)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.asType = asType;
		this.content = content;
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

	public IParsedType getAsType()
	{
		return asType;
	}

	public ParsedStatement getContent()
	{
		return content;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> IMember<E, T> compile(IDefinitionScope<E, T> scope)
	{
		return new SetterMember<E, T>(this, scope);
	}
}
