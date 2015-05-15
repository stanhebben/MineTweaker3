/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ReturnFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final CodePosition position;
	private final E value;
	
	public ReturnFlowInstruction(CodePosition position, E value)
	{
		this.position = position;
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
		if (value == null) {
			if (scope.getReturnType() != scope.getTypeCompiler().void_
					&& scope.getReturnType() != scope.getTypeCompiler().any)
				scope.getErrorLogger().errorMissingReturnValue(position);
		} else {
			if (!value.getType().canCastImplicit(scope, scope.getReturnType()))
				scope.getErrorLogger().errorCannotCastImplicit(position, value.getType(), scope.getReturnType());
		}
	}
}
