/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.member;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.AnyCallerMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedAnyCaller implements IParsedMember
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final ParsedFunctionSignature signature;
	private final ParsedStatement content;
	
	public ParsedAnyCaller(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			ParsedFunctionSignature signature,
			ParsedStatement content)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.signature = signature;
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

	public ParsedFunctionSignature getSignature()
	{
		return signature;
	}

	public ParsedStatement getContent()
	{
		return content;
	}

	@Override
	public <E extends IPartialExpression<E>> void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope)
	{
		
	}

	@Override
	public <E extends IPartialExpression<E>> IMember<E> compile(IDefinitionScope<E> scope)
	{
		return new AnyCallerMember<E>(this, scope);
	}
}
