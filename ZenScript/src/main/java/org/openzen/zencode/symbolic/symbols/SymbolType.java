/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolType<E extends IPartialExpression<E>> implements IZenSymbol<E>
{
	private final TypeInstance<E> type;

	public SymbolType(TypeInstance<E> type)
	{
		this.type = type;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> environment)
	{
		return new PartialType<E>(position, environment, type);
	}
}
