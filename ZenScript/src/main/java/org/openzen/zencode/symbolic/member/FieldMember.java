/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicUnit;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class FieldMember<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> implements IMember<E, T>
{
	private final ISymbolicUnit<E, T> unit;
	
	private final int access;
	private final String name;
	private final T type;
	
	public FieldMember(ISymbolicUnit<E, T> unit, int access, String name, T type)
	{
		this.unit = unit;
		this.access = access;
		this.name = name;
		this.type = type;
	}

	@Override
	public ISymbolicUnit<E, T> getUnit()
	{
		return unit;
	}
}
