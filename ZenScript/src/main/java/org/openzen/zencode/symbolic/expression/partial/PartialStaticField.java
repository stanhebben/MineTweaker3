/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticField<E extends IPartialExpression<E>>
	extends AbstractPartialExpression<E>
{
	private final IField<E> field;
	
	public PartialStaticField(CodePosition position, IMethodScope<E> scope, IField<E> field)
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
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return field.getType().getInstanceMethods();
	}
	
	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public TypeInstance<E> getType()
	{
		return field.getType();
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		// TODO: if we could get the value...
		return null;
	}
}
