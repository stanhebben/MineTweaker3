/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public abstract class AbstractPartialExpression<E extends IPartialExpression<E>> implements IPartialExpression<E>
{
	private final CodePosition position;
	private final IMethodScope<E> scope;
	
	public AbstractPartialExpression(CodePosition position, IMethodScope<E> scope)
	{
		this.position = position;
		this.scope = scope;
	}
	
	@Override
	public CodePosition getPosition()
	{
		return position;
	}
	
	@Override
	public IMethodScope<E> getScope()
	{
		return scope;
	}
	
	@Override
	public E cast(CodePosition position, TypeInstance<E> type)
	{
		ICastingRule<E> castingRule = getType().getCastingRule(type);
		if (castingRule == null)
		{
			getScope().getErrorLogger().errorCannotCastExplicit(position, getType(), type);
			return getScope().getExpressionCompiler().invalid(position, getScope());
		}
		
		return castingRule.cast(position, getScope(), eval());
	}
	
	@Override
	public void validate()
	{
		
	}
}
