/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.FunctionMember;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 *
 * @author Stan
 */
public class FunctionTypeDefinition<E extends IPartialExpression<E>> extends TypeDefinition<E>
{
	private final MethodHeader<E> methodHeader;
	
	public FunctionTypeDefinition(MethodHeader<E> methodHeader)
	{
		this.methodHeader = methodHeader;
		
		addMember(new FunctionMember<>(methodHeader));
	}
}
