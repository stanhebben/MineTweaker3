/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public class FlowBlock<E extends IPartialExpression<E>>
{
	private final List<IFlowInstruction<E>> instructions;
	private final List<FlowBlock<E>> outgoing;
	private final List<FlowBlock<E>> incoming;
	
	public FlowBlock()
	{
		instructions = new ArrayList<IFlowInstruction<E>>();
		outgoing = new ArrayList<FlowBlock<E>>();
		incoming = new ArrayList<FlowBlock<E>>();
	}
	
	public FlowBlock<E> prependInstruction(IFlowInstruction<E> instruction)
	{
		if (incoming.isEmpty()) {
			instructions.add(0, instruction);
			return this;
		} else {
			FlowBlock<E> newBlock = new FlowBlock<E>();
			newBlock.addOutgoing(this);
			return newBlock.prependInstruction(instruction);
		}
	}
	
	public void addOutgoing(FlowBlock<E> block)
	{
		this.outgoing.add(block);
		block.incoming.add(this);
	}
	
	public boolean doesFallthrough()
	{
		return instructions.isEmpty()
				|| instructions.get(instructions.size() - 1).doesFallthough();
	}
	
	public void validate(IMethodScope<E> scope, Set<FlowBlock<E>> validated)
	{
		for (IFlowInstruction<E> instruction : instructions) {
			instruction.validate(scope);
		}
	}
}
