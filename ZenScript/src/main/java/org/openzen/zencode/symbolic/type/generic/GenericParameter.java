/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.parser.generic.IParsedGenericBound;
import org.openzen.zencode.parser.generic.ParsedGenericParameter;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class GenericParameter<E extends IPartialExpression<E>> implements ITypeVariable<E>
{
	public static <ES extends IPartialExpression<ES>>
		 List<GenericParameter<ES>> compile(List<ParsedGenericParameter> parameters, IModuleScope<ES> scope)
	{
		if (parameters.isEmpty())
			return Collections.emptyList();
		
		List<GenericParameter<ES>> result = new ArrayList<>();
		for (ParsedGenericParameter parameter : parameters) {
			result.add(new GenericParameter<>(parameter, scope));
		}
		return result;
	}
	
	private final ParsedGenericParameter source;
	private final IModuleScope<E> scope;
	
	private final CodePosition position;
	private final String name;
	private final List<IGenericParameterBound<E>> bounds;
	
	public GenericParameter(ParsedGenericParameter source, IModuleScope<E> scope)
	{
		this.source = source;
		this.scope = scope;
		
		this.position = source.getPosition();
		this.name = source.getName();
		this.bounds = new ArrayList<>();
	}
	
	public GenericParameter(CodePosition position, String name, List<IGenericParameterBound<E>> bounds)
	{
		this.source = null;
		this.scope = null;
		
		this.position = position;
		this.name = name;
		this.bounds = bounds;
	}

	@Override
	public List<IGenericParameterBound<E>> getBounds()
	{
		return bounds;
	}
	
	public void completeMembers(IModuleScope<E> scope)
	{
		if (source == null)
			return;
		
		for (IParsedGenericBound bound : source.getBounds()) {
			bounds.add(bound.compile(scope, this));
		}
	}
	
	public void completeContents(IModuleScope<E> scope)
	{
		for (IGenericParameterBound<E> bound : bounds) {
			bound.completeContents(scope.getConstantScope());
		}
	}
}
