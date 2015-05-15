/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import org.openzen.zencode.symbolic.expression.IPartialExpression;

/**
 *
 * @author Stan
 * @param <E>
 * @param <R>
 */
public interface IMemberVisitor<E extends IPartialExpression<E>, R>
{
	public R onField(IFieldMember<E> member);
	
	public R onConstructor(IConstructorMember<E> member);
	
	public R onMethod(IMethodMember<E> member);
	
	public R onGetter(IGetterMember<E> getter);
	
	public R onSetter(ISetterMember<E> setter);
	
	public R onOperator(IOperatorMember<E> operator);
	
	public R onCaller(ICallerMember<E> caller);
	
	public R onImplementation(IImplementationMember<E> implementation);
	
	public R onCaster(ICasterMember<E> caster);
	
	public R onIterator(IIteratorMember<E> iterator);
}
