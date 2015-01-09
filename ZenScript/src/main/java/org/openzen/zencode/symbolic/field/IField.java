/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.field;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IField<E extends IPartialExpression<E>>
{
	public TypeInstance<E> getType();
	
	public boolean isFinal();
	
	public boolean isStatic();
	
	public E makeStaticGetExpression(CodePosition position, IMethodScope<E> scope);
	
	public E makeStaticSetExpression(CodePosition position, IMethodScope<E> scope, E value);
	
	public E makeInstanceGetExpression(CodePosition position, IMethodScope<E> scope, E target);
	
	public E makeInstanceSetExpression(CodePosition position, IMethodScope<E> scope, E target, E value);
}
