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
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticGetter;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class PartialStaticGetter<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
	extends AbstractPartialExpression<E, T>
{
	private final IMethod<E, T> method;
	
	public PartialStaticGetter(CodePosition position, IMethodScope<E, T> scope, IMethod<E, T> method)
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
	public IPartialExpression<E, T> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod<E, T>> getMethods()
	{
		return getType().getInstanceMethods();
	}
	
	@Override
	public IPartialExpression<E, T> call(CodePosition position, IMethod<E, T> method, List<E> arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public IZenSymbol<E, T> toSymbol()
	{
		return new SymbolStaticGetter<E, T>(method);
	}

	@Override
	public T getType()
	{
		return method.getReturnType();
	}

	@Override
	public T toType(List<T> genericTypes)
	{
		throw new UnsupportedOperationException("Cannot convert static getter to type");
	}

	@Override
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function)
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
