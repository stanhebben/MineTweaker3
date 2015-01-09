/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.ParsedAnnotation;
import org.openzen.zencode.parser.member.ParsedAccessor;
import org.openzen.zencode.parser.member.ParsedField;
import org.openzen.zencode.parser.modifier.IParsedModifier;
import org.openzen.zencode.parser.statement.ParsedStatement;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FieldMember<E extends IPartialExpression<E>> implements IMember<E>
{
	private final IDefinitionScope<E> unitScope;
	private final ParsedField source;
	private final int modifiers;
	
	private final String name;
	private final TypeInstance<E> type;
	
	private final FieldGetterMember<E> getter;
	private final FieldSetterMember<E> setter;
	
	private List<SymbolicAnnotation<E>> annotations;
	
	public FieldMember(IDefinitionScope<E> unitScope, int modifiers, String name, TypeInstance<E> type)
	{
		this.unitScope = unitScope;
		this.source = null;
		
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
		
		getter = makeDefaultGetter();
		setter = makeDefaultSetter();
	}
	
	public FieldMember(ParsedField source, IDefinitionScope<E> scope)
	{
		this.unitScope = scope;
		this.source = source;
		
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
		this.name = source.getName();
		this.type = source.getAsType().compile(scope);
		
		FieldGetterMember<E> _getter = makeDefaultGetter();
		FieldSetterMember<E> _setter = makeDefaultSetter();
		
		for (ParsedAccessor accessor : source.getAccessors()) {
			if (accessor.getType() == ParsedAccessor.Type.GET) {
				_getter = new FieldGetterMember<E>(this, accessor);
			} else {
				_setter = new FieldSetterMember<E>(this, accessor);
			}
		}
		
		this.getter = _getter;
		this.setter = _setter;
	}
	
	public TypeInstance<E> getType()
	{
		return type;
	}
	
	public IDefinitionScope<E> getUnitScope()
	{
		return unitScope;
	}

	@Override
	public ISymbolicDefinition<E> getUnit()
	{
		return unitScope.getDefinition();
	}

	@Override
	public void completeContents()
	{
		getter.completeContents();
		setter.completeContents();
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), unitScope);
	}

	@Override
	public void validate()
	{
		getter.validate();
		setter.validate();
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
	
	private FieldGetterMember<E> makeDefaultGetter()
	{
		ParsedStatement statement = ParsedStatement.parse("return $;", unitScope.getErrorLogger());
		ParsedAccessor accessor = new ParsedAccessor(
				source == null ? CodePosition.SYSTEM : source.getPosition(),
				ParsedAccessor.Type.GET,
				Collections.<ParsedAnnotation>emptyList(), // TODO: need to copy from field!
				Collections.<IParsedModifier>emptyList(),
				statement);
		return new FieldGetterMember<E>(this, accessor);
	}
	
	private FieldSetterMember<E> makeDefaultSetter()
	{
		ParsedStatement statement = ParsedStatement.parse("$ = value;", unitScope.getErrorLogger());
		ParsedAccessor accessor = new ParsedAccessor(
				source == null ? CodePosition.SYSTEM : source.getPosition(),
				ParsedAccessor.Type.SET,
				Collections.<ParsedAnnotation>emptyList(), // TODO: need to copy from field!
				Collections.<IParsedModifier>emptyList(),
				statement);
		return new FieldSetterMember<E>(this, accessor);
	}
}
