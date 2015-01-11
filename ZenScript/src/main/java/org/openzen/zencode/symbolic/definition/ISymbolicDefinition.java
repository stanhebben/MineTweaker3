/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.List;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ISymbolicDefinition<E extends IPartialExpression<E>>
{
	public int getModifiers();
	
	public List<SymbolicAnnotation<E>> getAnnotations();
	
	public void collectInnerDefinitions(List<ISymbolicDefinition<E>> units, IModuleScope<E> scope);
	
	public void compileMembers();
	
	public void compileMemberContents();
	
	public void validate();
	
	public List<? extends ITypeVariable<E>> getTypeVariables();
}
