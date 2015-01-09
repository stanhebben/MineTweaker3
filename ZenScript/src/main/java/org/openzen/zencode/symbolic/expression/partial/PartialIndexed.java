/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class PartialIndexed<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	extends AbstractPartialExpression<E, T>
{
	private final E value;
	private final E index;
	private final T asType;
	
	public PartialIndexed(CodePosition position, IMethodScope<E, T> scope, E value, E index, T asType)
	{
		super(position, scope);
		
		this.value = value;
		this.index = index;
		this.asType = asType;
	}
	
	@Override
	public E eval()
	{
		E result = value.getType().binary(getPosition(), getScope(), OperatorType.INDEXGET, value, index);
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public E assign(CodePosition position, E value)
	{
		E result = value.getType().ternary(getPosition(), getScope(), OperatorType.INDEXSET, value, index, value);
		
		if (asType != null)
			result = result.cast(getPosition(), asType);
		
		return result;
	}

	@Override
	public IPartialExpression<E, T> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod<E, T>> getMethods()
	{
		return eval().getMethods();
	}

	@Override
	public IPartialExpression<E, T> call(CodePosition position, IMethod<E, T> method, List<E> arguments)
	{
		return eval().call(position, method, arguments);
	}

	@Override
	public IZenSymbol<E, T> toSymbol()
	{
		return eval().toSymbol();
	}

	@Override
	public T getType()
	{
		return value.getType().getArrayBaseType();
	}

	@Override
	public T toType(List<T> genericTypes)
	{
		getScope().getErrorLogger().errorNotAType(getPosition(), this);
		return getScope().getTypeCompiler().getAny(getScope());
	}

	@Override
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny ctValue = value.getCompileTimeValue();
		if (ctValue == null)
			return null;
		
		IAny ctIndex = index.getCompileTimeValue();
		if (ctIndex == null)
			return null;
		
		return ctValue.indexGet(ctValue);
	}
}
