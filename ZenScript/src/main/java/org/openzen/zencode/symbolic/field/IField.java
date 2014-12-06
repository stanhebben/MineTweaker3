/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.field;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IField<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public T getType();
	
	public boolean isFinal();
	
	public boolean isStatic();
	
	public E makeStaticGetExpression(CodePosition position, IScopeMethod<E, T> scope);
	
	public E makeStaticSetExpression(CodePosition position, IScopeMethod<E, T> scope, E value);
	
	public E makeInstanceGetExpression(CodePosition position, IScopeMethod<E, T> scope, E target);
	
	public E makeInstanceSetExpression(CodePosition position, IScopeMethod<E, T> scope, E target, E value);
}
