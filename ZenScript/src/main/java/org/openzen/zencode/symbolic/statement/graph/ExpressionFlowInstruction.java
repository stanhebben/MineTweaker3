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
public class ExpressionFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final E expression;
	
	public ExpressionFlowInstruction(E expression)
	{
		this.expression = expression;
	}

	@Override
	public boolean doesFallthough()
	{
		return true;
	}

	@Override
	public void validate(IMethodScope<E> scope)
	{
		expression.validate();
	}
}
