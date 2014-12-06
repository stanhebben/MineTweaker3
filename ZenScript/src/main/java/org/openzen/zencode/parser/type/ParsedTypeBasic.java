/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IZenType;

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
	UBYTE("byte"),
	SHORT("short"),
	USHORT("ushort"),
	INT("int"),
	UINT("uint"),
	LONG("long"),
	ULONG("ulong"),
	FLOAT("float"),
	DOUBLE("double"),
	CHAR("char"),
	STRING("string");
	
	private final String name;
	
	private ParsedTypeBasic(String name)
	{
		this.name = name;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 T compile(IScopeGlobal<E, T> scope)
	{
		ITypeCompiler<E, T> types = scope.getTypes();
		
		switch (this) {
			case ANY:
				return types.getAny();
			case VOID:
				return types.getVoid();
			case BOOL:
				return types.getBool();
			case BYTE:
				return types.getByte();
			case UBYTE:
				return types.getUByte();
			case SHORT:
				return types.getShort();
			case USHORT:
				return types.getUShort();
			case INT:
				return types.getInt();
			case UINT:
				return types.getUInt();
			case LONG:
				return types.getLong();
			case ULONG:
				return types.getULong();
			case FLOAT:
				return types.getFloat();
			case DOUBLE:
				return types.getDouble();
			case CHAR:
				return types.getChar();
			case STRING:
				return types.getString();
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
