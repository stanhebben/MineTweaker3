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
public interface IFlowInstruction<E extends IPartialExpression<E>>
{
	public boolean doesFallthough();
	
	public void validate(IMethodScope<E> scope);
}
