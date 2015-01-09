/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.List;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IMember<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public ISymbolicDefinition<E, T> getUnit();
	
	public int getModifiers();
	
	public List<SymbolicAnnotation<E, T>> getAnnotations();
	
	public void completeContents();
	
	public void validate();
}
