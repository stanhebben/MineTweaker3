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

/**
 *
 * @author Stan
 * @param <E>
 */
public class FlowBuilder<E extends IPartialExpression<E>>
{
	private final Stack<Statement<E>> loops;
	private final Map<Statement<E>, FlowBlock<E>> breakLabels;
	private final Map<Statement<E>, FlowBlock<E>> continueLabels;
	
	public FlowBuilder()
	{
		loops = new Stack<Statement<E>>();
		breakLabels = new HashMap<Statement<E>, FlowBlock<E>>();
		continueLabels = new HashMap<Statement<E>, FlowBlock<E>>();
	}
	
	public void pushLoop(Statement<E> loop, FlowBlock<E> breakLabel, FlowBlock<E> continueLabel)
	{
		loops.push(loop);
		breakLabels.put(loop, breakLabel);
		continueLabels.put(loop, continueLabel);
	}
	
	public void pushSwitch(Statement<E> loop, FlowBlock<E> breakLabel)
	{
		loops.push(loop);
		breakLabels.put(loop, breakLabel);
	}
	
	public void pop()
	{
		loops.pop();
	}
	
	public FlowBlock<E> getBreakLabel(Statement<E> label)
	{
		return loops.isEmpty() ? createInvalidBlock() : breakLabels.get(label == null ? loops.peek() : label);
	}
	
	public FlowBlock<E> getContinueLabel(Statement<E> label)
	{
		return loops.isEmpty() ? createInvalidBlock() : breakLabels.get(label == null ? loops.peek() : label);
	}
	
	private FlowBlock<E> createInvalidBlock()
	{
		return new FlowBlock<E>();
	}
}
