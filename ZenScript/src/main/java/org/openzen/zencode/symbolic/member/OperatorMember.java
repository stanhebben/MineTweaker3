/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.parser.member.ParsedOperator;
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
public class OperatorMember<E extends IPartialExpression<E>>
	implements IMember<E>
{
	private final ParsedOperator source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	private final OperatorType operator;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public OperatorMember(IDefinitionScope<E> unit, int modifiers, OperatorType operator, MethodHeader<E> header)
	{
		this.source = null;
		this.modifiers = modifiers;
		this.operator = operator;
		this.methodScope = new MethodScope<E>(unit, header);
	}
	
	public OperatorMember(ParsedOperator source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		this.operator = source.getType();
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
			contents = source.getContent().compile(methodScope);
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
