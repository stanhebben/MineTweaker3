/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import java.util.HashSet;
import java.util.Set;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FlowGraph<E extends IPartialExpression<E>>
{
	private final FlowBlock<E> start;
	
	public FlowGraph(FlowBlock<E> start)
	{
		this.start = start;
	}
	
	public void validate(IMethodScope<E> scope)
	{
		// Validations:
		// - Check individual instructions
		// - Check return types
		// - Check if every path returns a value (unless the return type is void or any)
		// - Check if every used variable has an assigned value
		// - Check if final variables are not being assigned to
		
		Set<FlowBlock<E>> validated = new HashSet<FlowBlock<E>>();
		start.validate(scope, validated);
	}
}
