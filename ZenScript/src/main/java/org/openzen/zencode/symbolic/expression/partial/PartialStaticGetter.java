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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
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
	public List<IMethod<E>> getMethods()
	{
		return getType().getInstanceMethods();
	}
	
	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public IZenSymbol<E> toSymbol()
	{
		return new SymbolStaticGetter<E>(method);
	}

	@Override
	public TypeInstance<E> getType()
	{
		return method.getReturnType();
	}

	@Override
	public TypeInstance<E> toType(List<TypeInstance<E>> genericTypes)
	{
		throw new UnsupportedOperationException("Cannot convert static getter to type");
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
