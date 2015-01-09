/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public abstract class AbstractPartialExpression<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements IPartialExpression<E, T>
{
	private final CodePosition position;
	private final IMethodScope<E, T> scope;
	
	public AbstractPartialExpression(CodePosition position, IMethodScope<E, T> scope)
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
	public IMethodScope<E, T> getScope()
	{
		return scope;
	}
	
	@Override
	public E cast(CodePosition position, T type)
	{
		ICastingRule<E, T> castingRule = getType().getCastingRule(type);
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
