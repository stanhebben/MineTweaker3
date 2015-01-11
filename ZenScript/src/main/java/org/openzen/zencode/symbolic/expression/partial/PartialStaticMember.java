/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IGetter;
import org.openzen.zencode.symbolic.member.ISetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialStaticMember<E extends IPartialExpression<E>>
		extends AbstractPartialExpression<E>
{
	private final TypeInstance<E> target;

	private final String name;

	private IGetter<E> getter;
	private ISetter<E> setter;
	private final List<IMethod<E>> methods;

	public PartialStaticMember(CodePosition position, IMethodScope<E> scope, TypeInstance<E> target, String name)
	{
		super(position, scope);

		this.target = target;
		this.name = name;
		this.methods = new ArrayList<IMethod<E>>();
	}

	private PartialStaticMember(CodePosition position, IMethodScope<E> scope, PartialStaticMember<E> original)
	{
		super(position, scope);

		this.target = original.target;
		this.name = original.name;
		this.methods = original.methods;
	}

	public TypeInstance<E> getTarget()
	{
		return target;
	}

	public String getName()
	{
		return name;
	}

	public void setGetter(IGetter<E> getter)
	{
		this.getter = getter;
	}

	public void setSetter(ISetter<E> setter)
	{
		this.setter = setter;
	}

	public void addMethod(IMethod<E> method)
	{
		methods.add(method);
	}

	public boolean isEmpty()
	{
		return getter == null && setter == null && methods.isEmpty();
	}

	public PartialStaticMember<E> makeVariant(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialStaticMember<E>(position, scope, this);
	}

	@Override
	public E eval()
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");

		return getter.compileGet(getPosition(), getScope()).eval();
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		if (setter == null)
			throw new UnsupportedOperationException("This member is not a property or not writeable");

		return setter.compile(position, getScope(), other);
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		if (getter == null)
			throw new UnsupportedOperationException("This member is not a property or not readable");

		return getter.compileGet(getPosition(), getScope()).getMember(position, name);
	}

	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return method.callStatic(getPosition(), getScope(), arguments);
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return methods;
	}

	@Override
	public TypeInstance<E> getType()
	{
		if (getter == null)
			return null;

		return getter.getType();
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
