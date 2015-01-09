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
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class FlowGraph<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final FlowBlock<E, T> start;
	
	public FlowGraph(FlowBlock<E, T> start)
	{
		this.start = start;
	}
	
	public void validate(IMethodScope<E, T> scope)
	{
		// Validations:
		// - Check individual instructions
		// - Check return types
		// - Check if every path returns a value (unless the return type is void or any)
		// - Check if every used variable has an assigned value
		// - Check if final variables are not being assigned to
		
		Set<FlowBlock<E, T>> validated = new HashSet<FlowBlock<E, T>>();
		start.validate(scope, validated);
	}
}
