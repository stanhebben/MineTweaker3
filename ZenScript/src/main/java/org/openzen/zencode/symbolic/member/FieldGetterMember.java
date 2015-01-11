/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedAccessor;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FieldGetterMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final FieldMember<E> field;
	private final ParsedAccessor source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public FieldGetterMember(FieldMember<E> field, ParsedAccessor source)
	{
		this.field = field;
		this.source = source;
		
		MethodHeader<E> methodHeader = MethodHeader.noParameters(field.getType());
		methodScope = new MethodScope<E>(field.getUnitScope(), methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), field.getUnitScope().getErrorLogger());
	}
	
	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return field.getUnit();
	}

	@Override
	public void completeContents()
	{
		contents = source.getContent().compile(methodScope);
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
	}

	@Override
	public void validate()
	{
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
