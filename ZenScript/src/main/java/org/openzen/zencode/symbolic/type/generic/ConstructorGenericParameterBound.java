/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ConstructorGenericParameterBound<E extends IPartialExpression<E>>
	implements IGenericParameterBound<E>
{
	private final MethodHeader<E> header;
	
	public ConstructorGenericParameterBound(MethodHeader<E> header)
	{
		this.header = header;
	}

	@Override
	public void completeContents(IMethodScope<E> scope)
	{
		header.completeContents(scope);
	}
}
