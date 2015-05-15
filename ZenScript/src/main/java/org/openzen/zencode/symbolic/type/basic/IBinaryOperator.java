/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface IBinaryOperator<E extends IPartialExpression<E>>
{
	public E operator(CodePosition position, IMethodScope<E> scope, E left, E right);
}
