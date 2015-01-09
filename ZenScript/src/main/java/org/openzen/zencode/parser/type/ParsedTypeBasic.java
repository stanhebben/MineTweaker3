/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
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
		 T compile(IModuleScope<E, T> scope)
	{
		ITypeCompiler<E, T> types = scope.getTypeCompiler();
		
		switch (this) {
			case ANY:
				return types.getAny(scope);
			case VOID:
				return types.getVoid(scope);
			case BOOL:
				return types.getBool(scope);
			case BYTE:
				return types.getByte(scope);
			case UBYTE:
				return types.getUByte(scope);
			case SHORT:
				return types.getShort(scope);
			case USHORT:
				return types.getUShort(scope);
			case INT:
				return types.getInt(scope);
			case UINT:
				return types.getUInt(scope);
			case LONG:
				return types.getLong(scope);
			case ULONG:
				return types.getULong(scope);
			case FLOAT:
				return types.getFloat(scope);
			case DOUBLE:
				return types.getDouble(scope);
			case CHAR:
				return types.getChar(scope);
			case STRING:
				return types.getString(scope);
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
