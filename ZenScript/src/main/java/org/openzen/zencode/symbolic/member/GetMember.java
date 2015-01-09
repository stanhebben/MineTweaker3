/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedGetMember;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class GetMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final ParsedGetMember source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public GetMember(ParsedGetMember source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		MethodHeader<E> methodHeader = MethodHeader.noParameters(source.getAsType().compile(unitScope));
		methodScope = new MethodScope<E>(unitScope, methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return methodScope.getDefinition();
	}

	@Override
	public void completeContents()
	{
		methodScope.getMethodHeader().completeContents(methodScope);
		contents = source.getContent().compile(methodScope);
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
	}

	@Override
	public void validate()
	{
		contents.validate();
		methodScope.getMethodHeader().validate(methodScope);
	}

	@Override
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return annotations;
	}
}
