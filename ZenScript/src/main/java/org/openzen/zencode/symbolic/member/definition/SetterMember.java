/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedSetter;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.ISetterMember;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SetterMember<E extends IPartialExpression<E>> implements ISetterMember<E>
{
	private final IDefinitionScope<E> unitScope;
	private final ParsedSetter source;
	private final MethodHeader<E> methodHeader;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public SetterMember(ParsedSetter source, IDefinitionScope<E> scope)
	{
		this.source = source;
		this.unitScope = scope;
		methodHeader = MethodHeader.<E>singleParameter(
				scope.getTypeCompiler().void_,
				"value",
				source.getAsType().compile(scope));
		methodScope = new MethodScope<E>(unitScope, methodHeader, false);
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
	}
	
	@Override
	public String getName()
	{
		return source.getName();
	}
	
	@Override
	public E setStatic(CodePosition position, IMethodScope<E> scope, E value)
	{
		return scope.getExpressionCompiler().staticSetter(position, scope, this, value);
	}
	
	@Override
	public E setVirtual(CodePosition position, IMethodScope<E> scope, E instance, E value)
	{
		return scope.getExpressionCompiler().virtualSetter(position, scope, instance, this, value);
	}

	@Override
	public void completeContents()
	{
		contents = source.getContent().compile(methodScope);
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), unitScope);
	}

	@Override
	public void validate()
	{
		contents.validate();
		methodHeader.validate(methodScope);
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
		return visitor.onSetter(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
}
