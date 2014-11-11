/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;

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
	public ZenType compile(IScopeGlobal scope)
	{
		return new ZenTypeArrayBasic(baseType.compile(scope));
	}
	
	@Override
	public String toString()
	{
		return baseType.toString() + "[]";
	}
}
