/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ThrowFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
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
	public void validate(IMethodScope<E> scope)
	{
		// TODO: is this a valid exception?
		value.validate();
	}
}
