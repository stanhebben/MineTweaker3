/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedCaster;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.member.ICasterMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CasterMember<E extends IPartialExpression<E>> implements ICasterMember<E>
{
	private final ParsedCaster source;
	private final IMethodScope<E> methodScope;
	private final IGenericType<E> asType;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public CasterMember(ParsedCaster source, IDefinitionScope<E> scope)
	{
		this.source = source;
		asType = source.getAsType().compile(scope);
		
		MethodHeader<E> methodHeader = MethodHeader.noParameters(asType);
		methodScope = new MethodScope<E>(scope, methodHeader, false);
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
	}

	@Override
	public void completeContents()
	{
		contents = source.getBody().compile(methodScope);
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
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

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onCaster(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
}
