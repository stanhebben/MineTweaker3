/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;

/**
 *
 * @author Stan
 * @param <E>
 */
public class VarFlowInstruction<E extends IPartialExpression<E>> implements IFlowInstruction<E>
{
	private final SymbolLocal<E> symbol;
	private final E initializer;
	
	public VarFlowInstruction(SymbolLocal<E> symbol, E initializer)
	{
		this.symbol = symbol;
		this.initializer = initializer;
	}

	@Override
	public boolean doesFallthough()
	{
		return true;
	}

	@Override
	public void validate(IMethodScope<E> scope)
	{
		if (initializer != null) {
			initializer.validate();
			
			if (!initializer.getType().canCastImplicit(symbol.getType()))
				scope.getErrorLogger().errorCannotCastImplicit(initializer.getPosition(), initializer.getType(), symbol.getType());
		}
	}
}
