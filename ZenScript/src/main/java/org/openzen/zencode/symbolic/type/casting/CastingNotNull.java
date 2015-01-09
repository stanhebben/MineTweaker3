/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.type.casting;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class CastingNotNull<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		implements ICastingRule<E, T>
{
	private final T fromType;
	private final T bool;

	public CastingNotNull(T fromType, T bool)
	{
		this.fromType = fromType;
		this.bool = bool;
	}

	@Override
	public T getInputType()
	{
		return fromType;
	}

	@Override
	public T getResultingType()
	{
		return bool;
	}

	@Override
	public E cast(CodePosition position, IMethodScope<E, T> scope, E value)
	{
		return scope.getExpressionCompiler().notNull(position, scope, value);
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
