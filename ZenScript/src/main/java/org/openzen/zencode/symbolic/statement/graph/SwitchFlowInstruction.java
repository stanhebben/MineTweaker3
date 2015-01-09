/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SwitchFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final E value;
	private final FlowBlock<E> defaultBlock;
	private final Map<E, FlowBlock<E>> caseBlocks;
	
	public SwitchFlowInstruction(
			E value,
			FlowBlock<E> defaultBlock,
			Map<E, FlowBlock<E>> caseBlocks)
	{
		this.value = value;
		this.defaultBlock = defaultBlock;
		this.caseBlocks = caseBlocks;
	}

	@Override
	public boolean doesFallthough()
	{
		return false;
	}

	@Override
	public void validate(IMethodScope<E> scope)
	{
		// Does the given type support enums?
		if (!value.getType().isValidSwitchType())
			scope.getErrorLogger().errorInvalidSwitchValueType(value.getPosition(), value);
	}
}
