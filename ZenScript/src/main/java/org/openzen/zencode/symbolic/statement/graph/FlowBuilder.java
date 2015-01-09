/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class FlowBuilder<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final Stack<Statement<E, T>> loops;
	private final Map<Statement<E, T>, FlowBlock<E, T>> breakLabels;
	private final Map<Statement<E, T>, FlowBlock<E, T>> continueLabels;
	
	public FlowBuilder()
	{
		loops = new Stack<Statement<E, T>>();
		breakLabels = new HashMap<Statement<E, T>, FlowBlock<E, T>>();
		continueLabels = new HashMap<Statement<E, T>, FlowBlock<E, T>>();
	}
	
	public void pushLoop(Statement<E, T> loop, FlowBlock<E, T> breakLabel, FlowBlock<E, T> continueLabel)
	{
		loops.push(loop);
		breakLabels.put(loop, breakLabel);
		continueLabels.put(loop, continueLabel);
	}
	
	public void pushSwitch(Statement<E, T> loop, FlowBlock<E, T> breakLabel)
	{
		loops.push(loop);
		breakLabels.put(loop, breakLabel);
	}
	
	public void pop()
	{
		loops.pop();
	}
	
	public FlowBlock<E, T> getBreakLabel(Statement<E, T> label)
	{
		return loops.isEmpty() ? createInvalidBlock() : breakLabels.get(label == null ? loops.peek() : label);
	}
	
	public FlowBlock<E, T> getContinueLabel(Statement<E, T> label)
	{
		return loops.isEmpty() ? createInvalidBlock() : breakLabels.get(label == null ? loops.peek() : label);
	}
	
	private FlowBlock<E, T> createInvalidBlock()
	{
		return new FlowBlock<E, T>();
	}
}
