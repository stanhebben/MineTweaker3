/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 */
public class InstancedMethodHeader<E extends IPartialExpression<E>>
{
	private final IDefinitionScope<E> scope;
	private final TypeInstance<E> returnType;
	private final List<MethodParameter<E>> parameters;
	private final boolean isVararg;
	
	public InstancedMethodHeader(IDefinitionScope<E> scope, TypeInstance<E> returnType, List<MethodParameter<E>> parameters, boolean isVararg)
	{
		this.scope = scope;
		this.returnType = returnType;
		this.parameters = parameters;
		this.isVararg = isVararg;
	}
}
