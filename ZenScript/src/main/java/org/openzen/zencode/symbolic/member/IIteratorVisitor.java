/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IIteratorVisitor<E extends IPartialExpression<E>, R>
{
	public R onArrayKeyValueIterator();
	
	public R onArrayValueIterator();
	
	public R onMapKeysIterator();
	
	public R onMapKeyValuesIterator();
	
	public R onCustomIterator(TypeInstance<E> iteratorType);
	
	public R onRangeIterator();
}
