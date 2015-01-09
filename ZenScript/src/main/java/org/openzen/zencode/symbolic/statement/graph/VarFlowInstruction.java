/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.statement.graph;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class VarFlowInstruction<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IFlowInstruction<E, T>
{
	private final SymbolLocal<E, T> symbol;
	private final E initializer;
	
	public VarFlowInstruction(SymbolLocal<E, T> symbol, E initializer)
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
	public void validate(IMethodScope<E, T> scope)
	{
		if (initializer != null) {
			initializer.validate();
			
			if (!initializer.getType().canCastImplicit(symbol.getType()))
				scope.getErrorLogger().errorCannotCastImplicit(initializer.getPosition(), initializer.getType(), symbol.getType());
		}
	}
}
