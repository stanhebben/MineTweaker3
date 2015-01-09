/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ReturnFlowInstruction<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IFlowInstruction<E, T>
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
	public void validate(IMethodScope<E, T> scope)
	{
		if (value == null) {
			if (scope.getReturnType() != scope.getTypeCompiler().getVoid(scope)
					&& scope.getReturnType() != scope.getTypeCompiler().getAny(scope))
				scope.getErrorLogger().errorMissingReturnValue(position);
		} else {
			if (!value.getType().canCastImplicit(scope.getReturnType()))
				scope.getErrorLogger().errorCannotCastImplicit(position, value.getType(), scope.getReturnType());
		}
	}
}
