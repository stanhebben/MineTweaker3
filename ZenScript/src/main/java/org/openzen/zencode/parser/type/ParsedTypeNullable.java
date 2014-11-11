/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;

/**
 * Contains a parsed nullable type (type?).
 *
 * @author Stan Hebben
 */
public class ParsedTypeNullable implements IParsedType
{
	private final CodePosition position;
	private final IParsedType baseType;

	public ParsedTypeNullable(CodePosition position, IParsedType baseType)
	{
		this.position = position;
		this.baseType = baseType;
	}

	@Override
	public ZenType compile(IScopeGlobal scope)
	{
		ZenType cBaseType = baseType.compile(scope);
		ZenType type = cBaseType.nullable();
		if (type == null) {
			scope.error(position, baseType + " is not a nullable type");
			type = scope.getTypes().ANY;
		}
		return type;
	}
	
	@Override
	public String toString()
	{
		return baseType + "?";
	}
}
