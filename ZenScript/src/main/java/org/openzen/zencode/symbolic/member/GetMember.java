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
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class GetMember<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IMember<E, T>
{
	private final ParsedGetMember source;
	private final IMethodScope<E, T> methodScope;
	private final int modifiers;
	
	private Statement<E, T> contents;
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public GetMember(ParsedGetMember source, IDefinitionScope<E, T> unitScope)
	{
		this.source = source;
		MethodHeader<E, T> methodHeader = MethodHeader.noParameters(source.getAsType().compile(unitScope));
		methodScope = new MethodScope<E, T>(unitScope, methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E, T> getUnit()
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
	public List<SymbolicAnnotation<E, T>> getAnnotations()
	{
		return annotations;
	}
}
