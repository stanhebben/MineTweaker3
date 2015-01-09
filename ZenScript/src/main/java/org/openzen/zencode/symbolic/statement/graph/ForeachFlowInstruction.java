/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ForeachFlowInstruction<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IFlowInstruction<E, T>
{
	private final E values;
	private final FlowBlock<E, T> loopBody;
	private final FlowBlock<E, T> next;
	
	public ForeachFlowInstruction(E values, FlowBlock<E, T> loopBody, FlowBlock<E, T> next)
	{
		this.values = values;
		this.loopBody = loopBody;
		this.next = next;
	}
	
	@Override
	public boolean doesFallthough()
	{
		return true;
	}

	@Override
	public void validate(IMethodScope<E, T> scope)
	{
		
	}
}
