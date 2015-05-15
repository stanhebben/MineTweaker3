/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticGetter<E extends IPartialExpression<E>> extends AbstractPartialExpression<E>
{
	private final IMethod<E> method;
	
	public PartialStaticGetter(CodePosition position, IMethodScope<E> scope, IMethod<E> method)
	{
		super(position, scope);
		
		this.method = method;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E eval()
	{
		return method.callStatic(getPosition(), getScope(), Collections.<E>emptyList());
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		getScope().getErrorLogger().errorCannotAssignTo(position, this);
		return getScope().getExpressionCompiler().invalid(getPosition(), getScope());
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<ICallable<E>> getMethods()
	{
		return getType().getVirtualCallers(getScope(), eval());
	}

	@Override
	public IGenericType<E> getType()
	{
		return method.getReturnType();
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		// TODO: if we could get constant values...
		return null;
	}
}
