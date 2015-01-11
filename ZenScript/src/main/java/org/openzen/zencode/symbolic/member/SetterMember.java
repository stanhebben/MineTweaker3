/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedSetMember;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SetterMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final IDefinitionScope<E> unitScope;
	private final ParsedSetMember source;
	private final MethodHeader<E> methodHeader;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public SetterMember(ParsedSetMember source, IDefinitionScope<E> scope)
	{
		this.source = source;
		this.unitScope = scope;
		methodHeader = MethodHeader.<E>singleParameter(
				scope.getTypeCompiler().getVoid(scope),
				"value",
				source.getAsType().compile(scope));
		methodScope = new MethodScope<E>(unitScope, methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
	}

	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return unitScope.getDefinition();
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
}
