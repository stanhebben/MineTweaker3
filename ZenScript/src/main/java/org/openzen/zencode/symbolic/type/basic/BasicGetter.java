/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class BasicGetter<E extends IPartialExpression<E>> implements IGetterMember<E>
{
	private final IGenericType<E> type;
	private final String name;
	private final IUnaryOperator<E> getter;
	
	public BasicGetter(IGenericType<E> type, String name, IUnaryOperator<E> getter)
	{
		this.type = type;
		this.name = name;
		this.getter = getter;
	}
	
	@Override
	public IGenericType<E> getType()
	{
		return type;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public E getStatic(CodePosition position, IMethodScope<E> scope)
	{
		scope.getErrorLogger().errorNotAStaticMember(position, this);
		return scope.getExpressionCompiler().invalid(position, scope, type);
	}

	@Override
	public E getVirtual(CodePosition position, IMethodScope<E> scope, E instance)
	{
		return getter.operator(position, scope, instance);
	}

	@Override
	public int getModifiers()
	{
		return Modifier.EXPORT.getCode();
	}

	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return Collections.emptyList();
	}

	@Override
	public void completeContents()
	{
		
	}

	@Override
	public void validate()
	{
		
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return true;
	}

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onGetter(this);
	}
}
