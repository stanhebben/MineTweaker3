/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IMember<E extends IPartialExpression<E>>
{
	public int getModifiers();
	
	public List<SymbolicAnnotation<E>> getAnnotations();
	
	public void completeContents();
	
	public void validate();
	
	public boolean isAccessibleFrom(IModuleScope<E> scope);
	
	public <R> R accept(IMemberVisitor<E, R> visitor);
}
