/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.member.ParsedAccessor;
import org.openzen.zencode.parser.member.ParsedField;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IFieldMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FieldMember<E extends IPartialExpression<E>> implements IFieldMember<E>
{
	private final IDefinitionScope<E> unitScope;
	private final ParsedField source;
	private final int modifiers;
	
	private final String name;
	private final IGenericType<E> type;
	
	private int getterModifiers;
	private List<SymbolicAnnotation<E>> getterAnnotations;
	private int setterModifiers;
	private List<SymbolicAnnotation<E>> setterAnnotations;
	
	private List<SymbolicAnnotation<E>> annotations;
	
	public FieldMember(IDefinitionScope<E> unitScope, int modifiers, String name, IGenericType<E> type)
	{
		this.unitScope = unitScope;
		this.source = null;
		
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
		
		getterModifiers = modifiers;
		getterAnnotations = Collections.emptyList();
		setterModifiers = modifiers;
		setterAnnotations = Collections.emptyList();
	}
	
	public FieldMember(ParsedField source, IDefinitionScope<E> scope)
	{
		this.unitScope = scope;
		this.source = source;
		
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
		this.name = source.getName();
		this.type = source.getAsType().compile(scope);
		
		getterModifiers = modifiers;
		getterAnnotations = Collections.emptyList();
		setterModifiers = modifiers;
		setterAnnotations = Collections.emptyList();
		
		for (ParsedAccessor accessor : source.getAccessors()) {
			if (accessor.getType() == ParsedAccessor.Type.GET) {
				getterModifiers = Modifier.compileModifiers(accessor.getModifiers(), scope.getErrorLogger());
			} else {
				setterModifiers = Modifier.compileModifiers(accessor.getModifiers(), scope.getErrorLogger());
			}
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public IGenericType<E> getType()
	{
		return type;
	}
	
	public IDefinitionScope<E> getUnitScope()
	{
		return unitScope;
	}

	@Override
	public void completeContents()
	{
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), unitScope);
		
		for (ParsedAccessor accessor : source.getAccessors()) {
			if (accessor.getType() == ParsedAccessor.Type.GET) {
				getterAnnotations = SymbolicAnnotation.compileAll(accessor.getAnnotations(), unitScope);
			} else {
				setterAnnotations = SymbolicAnnotation.compileAll(accessor.getAnnotations(), unitScope);
			}
		}
	}

	@Override
	public void validate()
	{
		for (SymbolicAnnotation<E> annotation : annotations) {
			annotation.validate();
		}
		
		for (SymbolicAnnotation<E> annotation : getterAnnotations) {
			annotation.validate();
		}
		
		for (SymbolicAnnotation<E> annotation : setterAnnotations) {
			annotation.validate();
		}
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
		return visitor.onField(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return unitScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
}
