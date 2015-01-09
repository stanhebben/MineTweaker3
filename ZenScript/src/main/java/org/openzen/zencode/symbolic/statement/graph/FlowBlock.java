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
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class FlowBlock<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	private final List<IFlowInstruction<E, T>> instructions;
	private final List<FlowBlock<E, T>> outgoing;
	private final List<FlowBlock<E, T>> incoming;
	
	public FlowBlock()
	{
		instructions = new ArrayList<IFlowInstruction<E, T>>();
		outgoing = new ArrayList<FlowBlock<E, T>>();
		incoming = new ArrayList<FlowBlock<E, T>>();
	}
	
	public FlowBlock<E, T> prependInstruction(IFlowInstruction<E, T> instruction)
	{
		if (incoming.isEmpty()) {
			instructions.add(0, instruction);
			return this;
		} else {
			FlowBlock<E, T> newBlock = new FlowBlock<E, T>();
			newBlock.addOutgoing(this);
			return newBlock.prependInstruction(instruction);
		}
	}
	
	public void addOutgoing(FlowBlock<E, T> block)
	{
		this.outgoing.add(block);
		block.incoming.add(this);
	}
	
	public boolean doesFallthrough()
	{
		return instructions.isEmpty()
				|| instructions.get(instructions.size() - 1).doesFallthough();
	}
	
	public void validate(IMethodScope<E, T> scope, Set<FlowBlock<E, T>> validated)
	{
		for (IFlowInstruction<E, T> instruction : instructions) {
			instruction.validate(scope);
		}
	}
}
