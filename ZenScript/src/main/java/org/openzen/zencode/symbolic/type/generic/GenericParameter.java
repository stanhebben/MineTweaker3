/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.elements.IParsedGenericBound;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class GenericParameter<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements ITypeVariable<E, T>
{
	public static <ES extends IPartialExpression<ES, TS>, TS extends ITypeInstance<ES, TS>>
		 List<GenericParameter<ES, TS>> compile(List<ParsedGenericParameter> parameters, IModuleScope<ES, TS> scope)
	{
		if (parameters.isEmpty())
			return Collections.emptyList();
		
		List<GenericParameter<ES, TS>> result = new ArrayList<GenericParameter<ES, TS>>();
		for (ParsedGenericParameter parameter : parameters) {
			result.add(new GenericParameter<ES, TS>(parameter, scope));
		}
		return result;
	}
	
	private final ParsedGenericParameter source;
	private final IModuleScope<E, T> scope;
	
	private final CodePosition position;
	private final String name;
	private final List<IGenericParameterBound<E, T>> bounds;
	
	public GenericParameter(ParsedGenericParameter source, IModuleScope<E, T> scope)
	{
		this.source = source;
		this.scope = scope;
		
		this.position = source.getPosition();
		this.name = source.getName();
		this.bounds = new ArrayList<IGenericParameterBound<E, T>>();
	}
	
	public GenericParameter(CodePosition position, String name, List<IGenericParameterBound<E, T>> bounds)
	{
		this.source = null;
		this.scope = null;
		
		this.position = position;
		this.name = name;
		this.bounds = bounds;
	}

	@Override
	public List<IGenericParameterBound<E, T>> getBounds()
	{
		return bounds;
	}
	
	public void completeMembers(IModuleScope<E, T> scope)
	{
		if (source == null)
			return;
		
		for (IParsedGenericBound bound : source.getBounds()) {
			bounds.add(bound.compile(scope));
		}
	}
	
	public void completeContents(IModuleScope<E, T> scope)
	{
		for (IGenericParameterBound<E, T> bound : bounds) {
			bound.completeContents(scope.getConstantEnvironment());
		}
	}
}
