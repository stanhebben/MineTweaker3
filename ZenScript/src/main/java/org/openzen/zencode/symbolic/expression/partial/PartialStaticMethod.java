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
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.type.CallableFunctionType;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticMethod<E extends IPartialExpression<E>>
	extends AbstractPartialExpression<E>
{
	private final ICallable<E> method;
	
	public PartialStaticMethod(CodePosition position, IMethodScope<E> scope, ICallable<E> method)
	{
		super(position, scope);
		
		this.method = method;
	}

	@Override
	public E eval()
	{
		return method.asValue(getPosition(), getScope());
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		getScope().getErrorLogger().errorCannotAssignTo(position, this);
		return getScope().getExpressionCompiler().invalid(getPosition(), getScope(), other.getType());
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		getScope().getErrorLogger().errorFunctionHasNoMembers(position);
		return getScope().getExpressionCompiler().invalid(position, getScope());
	}
	
	@Override
	public List<ICallable<E>> getMethods()
	{
		return Collections.singletonList(method);
	}

	@Override
	public IGenericType<E> getType()
	{
		return new CallableFunctionType<E>(method);
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}
}
