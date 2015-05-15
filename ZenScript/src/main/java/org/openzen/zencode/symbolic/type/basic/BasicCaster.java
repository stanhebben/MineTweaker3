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
import org.openzen.zencode.symbolic.member.ICasterMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class BasicCaster<E extends IPartialExpression<E>> implements ICasterMember<E>
{
	private final IGenericType<E> fromType;
	private final IGenericType<E> toType;
	private final IUnaryOperator<E> caster;
	
	public BasicCaster(IGenericType<E> fromType, IGenericType<E> toType, IUnaryOperator<E> caster)
	{
		this.fromType = fromType;
		this.toType = toType;
		this.caster = caster;
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
		// nothing to do
	}

	@Override
	public void validate()
	{
		// nothing to do
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return true;
	}

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onCaster(this);
	}
}
