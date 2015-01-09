/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.parser.elements.ParsedGenericBoundExtends;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ExtendsGenericParameterBound<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IGenericParameterBound<E, T>
{
	private T type;
	
	public ExtendsGenericParameterBound(ParsedGenericBoundExtends source, IModuleScope<E, T> scope)
	{
		type = source.getType().compile(scope);
	}
	
	public ExtendsGenericParameterBound(T type)
	{
		this.type = type;
	}

	@Override
	public void completeContents(IMethodScope<E, T> scope)
	{
		// nothing to do
	}
}
