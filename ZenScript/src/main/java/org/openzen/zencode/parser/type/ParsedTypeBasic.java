/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public enum ParsedTypeBasic implements IParsedType
{
	ANY("any"),
	VOID("void"),
	BOOL("bool"),
	BYTE("byte"),
	SHORT("short"),
	INT("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	STRING("string");
	
	private final String name;
	
	private ParsedTypeBasic(String name)
	{
		this.name = name;
	}

	@Override
	public ZenType compile(IScopeGlobal scope)
	{
		TypeRegistry types = scope.getTypes();

		switch (this) {
			case ANY:
				return types.ANY;
			case VOID:
				return types.VOID;
			case BOOL:
				return types.BOOL;
			case BYTE:
				return types.BYTE;
			case SHORT:
				return types.SHORT;
			case INT:
				return types.INT;
			case LONG:
				return types.LONG;
			case FLOAT:
				return types.FLOAT;
			case DOUBLE:
				return types.DOUBLE;
			case STRING:
				return types.STRING;
			default:
				throw new AssertionError("Missing enum value: " + this);
		}
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
