/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.definition;

import java.util.Collection;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IImportable<E extends IPartialExpression<E>>
{
	public IImportable<E> getSubDefinition(String name);
	
	public Collection<String> getSubDefinitionNames();
	
	public IZenSymbol<E> getMember(String name);
	
	public IGenericType<E> toType(IModuleScope<E> scope, List<IGenericType<E>> genericTypes);
	
	public IPartialExpression<E> toPartialExpression(CodePosition position, IMethodScope<E> scope);
}
