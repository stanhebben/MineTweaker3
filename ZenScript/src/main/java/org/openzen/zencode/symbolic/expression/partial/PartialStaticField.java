/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticField;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticField<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	extends AbstractPartialExpression<E, T>
{
	private final IField<E, T> field;
	
	public PartialStaticField(CodePosition position, IScopeMethod<E, T> scope, IField<E, T> field)
	{
		super(position, scope);
		
		this.field = field;
	}

	@Override
	public E eval()
	{
		return field.makeStaticGetExpression(getPosition(), getScope());
	}

	@Override
	public E assign(CodePosition position, E value)
	{
		return field.makeStaticSetExpression(getPosition(), getScope(), value);
	}

	@Override
	public IPartialExpression<E, T> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod<E, T>> getMethods()
	{
		return field.getType().getInstanceMethods();
	}
	
	@Override
	public IPartialExpression<E, T> call(CodePosition position, IMethod<E, T> method, E... arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public IZenSymbol<E, T> toSymbol()
	{
		return new SymbolStaticField<E, T>(field);
	}

	@Override
	public T getType()
	{
		return field.getType();
	}

	@Override
	public T toType(List<T> genericTypes)
	{
		getScope().error(getPosition(), "Cannot convert static field to type");
		return getScope().getTypes().getAny();
	}

	@Override
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function)
	{
		return this;
	}
}
