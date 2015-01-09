/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.type.casting;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CastingNotNull<E extends IPartialExpression<E>> implements ICastingRule<E>
{
	private final TypeInstance<E> fromType;
	private final TypeInstance<E> bool;

	public CastingNotNull(TypeInstance<E> fromType, TypeInstance<E> bool)
	{
		this.fromType = fromType;
		this.bool = bool;
	}

	@Override
	public TypeInstance<E> getInputType()
	{
		return fromType;
	}

	@Override
	public TypeInstance<E> getResultingType()
	{
		return bool;
	}

	@Override
	public E cast(CodePosition position, IMethodScope<E> scope, E value)
	{
		return scope.getExpressionCompiler().notNull(position, scope, value);
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
