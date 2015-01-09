/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedFunctionMember;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.symbolic.statement.Statement;

/**
 *
 * @author Stan
 * @param <E>
 */
public class MethodMember<E extends IPartialExpression<E>>
	implements IMember<E>
{
	private final ParsedFunctionMember source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	private final String name;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public MethodMember(IDefinitionScope<E> unit, int modifiers, String name, MethodHeader<E> header)
	{
		this.source = null;
		this.modifiers = modifiers;
		this.name = name;
		this.methodScope = new MethodScope<E>(unit, header);
	}
	
	public MethodMember(ParsedFunctionMember source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		this.name = source.getName();
		MethodHeader<E> header = source.getSignature().compile(unitScope);
		this.methodScope = new MethodScope<E>(unitScope, header);
	}
	
	public MethodHeader<E> getHeader()
	{
		return methodScope.getMethodHeader();
	}

	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return methodScope.getDefinition();
	}

	@Override
	public void completeContents()
	{
		if (source != null)
		{
			contents = source.getContents().compile(methodScope);
			annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
		}
	}

	@Override
	public void validate()
	{
		methodScope.getMethodHeader().validate(methodScope);
		contents.validate();
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
