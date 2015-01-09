/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialLocal;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class SymbolLocal<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IZenSymbol<E, T>
{
	private final T type;
	private final boolean isFinal;

	public SymbolLocal(T type, boolean isFinal)
	{
		this.type = type;
		this.isFinal = isFinal;
	}

	public T getType()
	{
		return type;
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IMethodScope<E, T> environment)
	{
		return new PartialLocal<E, T>(position, environment, this);
	}
}
