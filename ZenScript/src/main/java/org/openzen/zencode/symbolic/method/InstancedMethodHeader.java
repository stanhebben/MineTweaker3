/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 */
public class InstancedMethodHeader<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final IDefinitionScope<E, T> scope;
	private final T returnType;
	private final List<MethodParameter<E, T>> parameters;
	private final boolean isVararg;
	
	public InstancedMethodHeader(IDefinitionScope<E, T> scope, T returnType, List<MethodParameter<E, T>> parameters, boolean isVararg)
	{
		this.scope = scope;
		this.returnType = returnType;
		this.parameters = parameters;
		this.isVararg = isVararg;
	}
}
