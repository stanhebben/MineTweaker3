/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.definition.ParsedFunctionSignature;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.definition.ConstructorMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedConstructor implements IParsedMember
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final ParsedFunctionSignature signature;
	private final ParsedStatement contents;
	
	public ParsedConstructor(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			ParsedFunctionSignature signature,
			ParsedStatement contents)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.signature = signature;
		this.contents = contents;
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

	public ParsedFunctionSignature getSignature()
	{
		return signature;
	}

	public ParsedStatement getContents()
	{
		return contents;
	}

	@Override
	public <E extends IPartialExpression<E>> void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
	{
		
	}

	@Override
	public <E extends IPartialExpression<E>> IMember<E> compile(IDefinitionScope<E> scope)
	{
		return new ConstructorMember<E>(this, scope);
	}
}
