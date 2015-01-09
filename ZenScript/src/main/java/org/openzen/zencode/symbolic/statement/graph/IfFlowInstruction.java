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
public class IfFlowInstruction<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IFlowInstruction<E, T>
{
	private final E condition;
	private final FlowBlock<E, T> onIf;
	private final FlowBlock<E, T> onElse;
	
	public IfFlowInstruction(E condition, FlowBlock<E, T> onIf, FlowBlock<E, T> onElse)
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
	public void validate(IMethodScope<E, T> scope)
	{
		T type = condition.getType();
		if (!type.canCastImplicit(scope.getTypeCompiler().getBool(scope)))
			scope.getErrorLogger().errorCannotCastImplicit(condition.getPosition(), type, scope.getTypeCompiler().getBool(scope));
	}
}
