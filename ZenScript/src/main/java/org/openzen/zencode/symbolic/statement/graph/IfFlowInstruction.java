/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public class IfFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final E condition;
	private final FlowBlock<E> onIf;
	private final FlowBlock<E> onElse;
	
	public IfFlowInstruction(E condition, FlowBlock<E> onIf, FlowBlock<E> onElse)
	{
		this.condition = condition;
		this.onIf = onIf;
		this.onElse = onElse;
	}

	@Override
	public boolean doesFallthough()
	{
		return false;
	}

	@Override
	public void validate(IMethodScope<E> scope)
	{
		IGenericType<E> type = condition.getType();
		if (!type.canCastImplicit(scope, scope.getTypeCompiler().bool))
			scope.getErrorLogger().errorCannotCastImplicit(condition.getPosition(), type, scope.getTypeCompiler().bool);
	}
}
