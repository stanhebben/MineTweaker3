/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class GenericParameter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final CodePosition position;
	private final String name;
	private final List<IGenericParameterBound<E, T>> bounds;
	
	public GenericParameter(CodePosition position, String name, List<IGenericParameterBound<E, T>> bounds)
	{
		this.position = position;
		this.name = name;
		this.bounds = bounds;
	}
	
	public void completeContents(IMethodScope<E, T> scope)
	{
		for (IGenericParameterBound<E, T> bound : bounds) {
			bound.completeContents(scope);
		}
	}
}
