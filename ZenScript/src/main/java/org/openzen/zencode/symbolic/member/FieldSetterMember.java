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
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FieldSetterMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final FieldMember<E> field;
	private final ParsedAccessor source;
	private final int modifiers;
	
	private final IMethodScope<E> methodScope;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public FieldSetterMember(FieldMember<E> field, ParsedAccessor source)
	{
		this.field = field;
		this.source = source;
		
		IDefinitionScope<E> unitScope = field.getUnitScope();
		MethodHeader<E> methodHeader = MethodHeader.singleParameter(
				unitScope.getTypeCompiler().getVoid(unitScope),
				"value",
				field.getType());
		methodScope = new MethodScope<E>(field.getUnitScope(), methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}
	
	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return field.getUnit();
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
