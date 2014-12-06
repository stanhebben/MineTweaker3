/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 * Parsed array type (valuetype[]).
 * 
 * @author Stan Hebben
 */
public class ParsedTypeArray implements IParsedType
{
	private final IParsedType baseType;

	public ParsedTypeArray(IParsedType baseType)
	{
		this.baseType = baseType;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 T compile(IScopeGlobal<E, T> scope)
	{
		return scope.getTypes().getArray(baseType.compile(scope));
	}
	
	@Override
	public String toString()
	{
		return baseType.toString() + "[]";
	}
}
