/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.parser.definition;

import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.elements.ParsedFunctionSignature;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ParsedFunction implements IParsedDefinition
{
	private final CodePosition position;
	private final List<ParsedAnnotation> annotations;
	private final List<IParsedModifier> modifiers;
	private final String name;
	private final ParsedFunctionSignature signature;
	private final ParsedStatement contents;
	
	public ParsedFunction(
			CodePosition position,
			List<ParsedAnnotation> annotations,
			List<IParsedModifier> modifiers,
			String name,
			ParsedFunctionSignature signature,
			ParsedStatement contents)
	{
		this.position = position;
		this.annotations = annotations;
		this.modifiers = modifiers;
		this.name = name;
		this.signature = signature;
		this.contents = contents;
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

	public ParsedFunctionSignature getSignature()
	{
		return signature;
	}

	public ParsedStatement getContents()
	{
		return contents;
	}

	@Override
	public <E extends IPartialExpression<E>> ISymbolicDefinition<E> compile(IModuleScope<E> scope)
	{
		return new SymbolicFunction<E>(this, scope);
	}

	@Override
	public List<ParsedGenericParameter> getGenericParameters()
	{
		return signature.getGenericParameters();
	}
}
