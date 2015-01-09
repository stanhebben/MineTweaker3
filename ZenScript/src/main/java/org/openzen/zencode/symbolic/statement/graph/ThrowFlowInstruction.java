/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ThrowFlowInstruction<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IFlowInstruction<E, T>
{
	private final E value;
	
	public ThrowFlowInstruction(E value)
	{
		this.value = value;
	}

	@Override
	public boolean doesFallthough()
	{
		return false;
	}

	@Override
	public void validate(IMethodScope<E, T> scope)
	{
		// TODO: is this a valid exception?
		value.validate();
	}
}
