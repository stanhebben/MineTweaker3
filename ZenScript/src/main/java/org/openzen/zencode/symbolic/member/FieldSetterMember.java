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
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class FieldSetterMember<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IMember<E, T>
{
	private final FieldMember<E, T> field;
	private final ParsedAccessor source;
	private final int modifiers;
	
	private final IMethodScope<E, T> methodScope;
	
	private Statement<E, T> contents;
	private List<SymbolicAnnotation<E, T>> annotations;
	
	public FieldSetterMember(FieldMember<E, T> field, ParsedAccessor source)
	{
		this.field = field;
		this.source = source;
		
		IDefinitionScope<E, T> unitScope = field.getUnitScope();
		MethodHeader<E, T> methodHeader = MethodHeader.singleParameter(
				unitScope.getTypeCompiler().getVoid(unitScope),
				"value",
				field.getType());
		methodScope = new MethodScope<E, T>(field.getUnitScope(), methodHeader);
		modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
	}
	
	@Override
	public ISymbolicDefinition<E, T> getUnit()
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
	public List<SymbolicAnnotation<E, T>> getAnnotations()
	{
		return annotations;
	}
}
