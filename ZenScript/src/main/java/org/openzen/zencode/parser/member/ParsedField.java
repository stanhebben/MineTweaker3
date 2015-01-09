/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.expression.ParsedExpression;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.type.IParsedType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.FieldMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedField implements IParsedMember
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final IParsedType asType;
	private final ParsedExpression initializer;
	private final List<ParsedAccessor> accessors;
	private final boolean isFinal;
	
	public ParsedField(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			IParsedType asType,
			ParsedExpression initializer,
			List<ParsedAccessor> accessors,
			boolean isFinal)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.asType = asType;
		this.initializer = initializer;
		this.accessors = accessors;
		this.isFinal = isFinal;
	}

	public CodePosition getPosition()
	{
		return position;
	}
	
	public boolean isFinal()
	{
		return isFinal;
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

	public ParsedExpression getInitializer()
	{
		return initializer;
	}

	public List<ParsedAccessor> getAccessors()
	{
		return accessors;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> void collectInnerDefinitions(List<ISymbolicDefinition<E, T>> units, IModuleScope<E, T> scope)
	{
		
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>> IMember<E, T> compile(IDefinitionScope<E, T> scope)
	{
		return new FieldMember<E, T>(this, scope);
	}
}
