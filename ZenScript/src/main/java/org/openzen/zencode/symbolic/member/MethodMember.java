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
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class MethodMember<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	implements IMember<E, T>
{
	private final ParsedFunctionMember source;
	private final IMethodScope<E, T> methodScope;
	private final int modifiers;
	private final String name;
	
	private Statement<E, T> contents;
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public MethodMember(IDefinitionScope<E, T> unit, int modifiers, String name, MethodHeader<E, T> header)
	{
		this.source = null;
		this.modifiers = modifiers;
		this.name = name;
		this.methodScope = new MethodScope<E, T>(unit, header);
	}
	
	public MethodMember(ParsedFunctionMember source, IDefinitionScope<E, T> unitScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		this.name = source.getName();
		MethodHeader<E, T> header = source.getSignature().compile(unitScope);
		this.methodScope = new MethodScope<E, T>(unitScope, header);
	}
	
	public MethodHeader<E, T> getHeader()
	{
		return methodScope.getMethodHeader();
	}

	@Override
	public ISymbolicDefinition<E, T> getUnit()
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
	public List<SymbolicAnnotation<E, T>> getAnnotations()
	{
		return annotations;
	}
}
