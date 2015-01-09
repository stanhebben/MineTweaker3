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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialIndexed<E extends IPartialExpression<E>>
	extends AbstractPartialExpression<E>
{
	private final E value;
	private final E index;
	private final TypeInstance<E> asType;
	
	public PartialIndexed(CodePosition position, IMethodScope<E> scope, E value, E index, TypeInstance<E> asType)
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
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return eval().getMethods();
	}

	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return eval().call(position, method, arguments);
	}

	@Override
	public IZenSymbol<E> toSymbol()
	{
		return eval().toSymbol();
	}

	@Override
	public TypeInstance<E> getType()
	{
		return value.getType().getArrayBaseType();
	}

	@Override
	public TypeInstance<E> toType(List<TypeInstance<E>> genericTypes)
	{
		getScope().getErrorLogger().errorNotAType(getPosition(), this);
		return getScope().getTypeCompiler().getAny(getScope());
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
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
