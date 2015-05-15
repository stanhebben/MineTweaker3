/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import org.openzen.zencode.parser.generic.ParsedGenericBoundImplements;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ImplementsGenericParameterBound<E extends IPartialExpression<E>>
	implements IGenericParameterBound<E>
{
	private IGenericType<E> type;
	
	public ImplementsGenericParameterBound(ParsedGenericBoundImplements source, IModuleScope<E> scope)
	{
		type = source.getType().compile(scope);
	}
	
	public ImplementsGenericParameterBound(IGenericType<E> type)
	{
		this.type = type;
	}

	@Override
	public IMember<E> getMember()
	{
		return null;
	}

	@Override
	public IGenericType<E> getBound()
	{
		return type;
	}

	@Override
	public void completeContents(IMethodScope<E> scope)
	{
		// nothing to do
	}
}
