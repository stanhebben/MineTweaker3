/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.expression.partial.PartialType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolType<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		implements IZenSymbol<E, T>
{
	private final T type;

	public SymbolType(T type)
	{
		this.type = type;
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IMethodScope<E, T> environment)
	{
		return new PartialType<E, T>(position, environment, type);
	}
}
