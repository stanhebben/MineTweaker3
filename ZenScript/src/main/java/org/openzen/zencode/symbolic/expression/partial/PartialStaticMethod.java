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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticMethod<E extends IPartialExpression<E>>
	extends AbstractPartialExpression<E>
{
	private final IMethod<E> method;
	
	public PartialStaticMethod(CodePosition position, IMethodScope<E> scope, IMethod<E> method)
	{
		super(position, scope);
		
		this.method = method;
	}

	@Override
	public E eval()
	{
		return getScope().getExpressionCompiler().staticMethodValue(getPosition(), getScope(), method);
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
		return method.getFunctionType().getInstanceMember(position, getScope(), eval(), name);
	}
	
	@Override
	public List<IMethod<E>> getMethods()
	{
		return Collections.singletonList(method);
	}
	
	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return method.callStatic(position, getScope(), arguments);
	}

	@Override
	public TypeInstance<E> getType()
	{
		return method.getFunctionType();
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
