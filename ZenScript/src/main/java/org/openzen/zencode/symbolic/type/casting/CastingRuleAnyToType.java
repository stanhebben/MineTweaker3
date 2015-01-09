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
public class CastingRuleAnyToType<E extends IPartialExpression<E>> implements ICastingRule<E>
{
	private final TypeInstance<E> fromType;
	private final TypeInstance<E> toType;
	
	public CastingRuleAnyToType(TypeInstance<E> fromType, TypeInstance<E> toType)
	{
		this.fromType = fromType;
		this.toType = toType;
	}

	@Override
	public E cast(CodePosition position, IMethodScope<E> scope, E value)
	{
		return scope.getExpressionCompiler().anyCastTo(position, scope, value, toType);
	}

	@Override
	public TypeInstance<E> getInputType()
	{
		return fromType;
	}

	@Override
	public TypeInstance<E> getResultingType()
	{
		return toType;
	}

	@Override
	public boolean isExplicit()
	{
		return false;
	}
}
