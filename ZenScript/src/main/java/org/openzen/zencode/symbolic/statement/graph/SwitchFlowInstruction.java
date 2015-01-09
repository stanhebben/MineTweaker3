/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SwitchFlowInstruction<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IFlowInstruction<E, T>
{
	private final E value;
	private final FlowBlock<E, T> defaultBlock;
	private final Map<E, FlowBlock<E, T>> caseBlocks;
	
	public SwitchFlowInstruction(
			E value,
			FlowBlock<E, T> defaultBlock,
			Map<E, FlowBlock<E, T>> caseBlocks)
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
	public void validate(IMethodScope<E, T> scope)
	{
		// Does the given type support enums?
		if (!value.getType().isValidSwitchType())
			scope.getErrorLogger().errorInvalidSwitchValueType(value.getPosition(), value);
	}
}
