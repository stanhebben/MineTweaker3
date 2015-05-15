/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedGetter;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class GetterMember<E extends IPartialExpression<E>> implements IGetterMember<E>
{
	private final ParsedGetter source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public GetterMember(ParsedGetter source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		MethodHeader<E> methodHeader = MethodHeader.noParameters(source.getAsType().compile(unitScope));
		methodScope = new MethodScope<E>(unitScope, methodHeader, false);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}
	
	@Override
	public String getName()
	{
		return source.getName();
	}
	
	@Override
	public IGenericType<E> getType()
	{
		return methodScope.getReturnType();
	}
	
	@Override
	public E getStatic(CodePosition position, IMethodScope<E> scope)
	{
		return scope.getExpressionCompiler().staticGetter(position, scope, this);
	}
	
	@Override
	public E getVirtual(CodePosition position, IMethodScope<E> scope, E instance)
	{
		return scope.getExpressionCompiler().virtualGetter(position, scope, instance, this);
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

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onGetter(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
}
