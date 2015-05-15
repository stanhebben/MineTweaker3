/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;

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
	STRING("string"),
	RANGE("range");
	
	private final String name;
	
	private ParsedTypeBasic(String name)
	{
		this.name = name;
	}

	@Override
	public <E extends IPartialExpression<E>>
		 IGenericType<E> compile(IModuleScope<E> scope)
	{
		TypeRegistry<E> types = scope.getTypeCompiler();
		
		switch (this) {
			case ANY:
				return types.any;
			case VOID:
				return types.void_;
			case BOOL:
				return types.bool;
			case BYTE:
				return types.byte_;
			case UBYTE:
				return types.ubyte;
			case SHORT:
				return types.short_;
			case USHORT:
				return types.ushort;
			case INT:
				return types.int_;
			case UINT:
				return types.uint;
			case LONG:
				return types.long_;
			case ULONG:
				return types.ulong;
			case FLOAT:
				return types.float_;
			case DOUBLE:
				return types.double_;
			case CHAR:
				return types.char_;
			case STRING:
				return types.string;
			case RANGE:
				return types.range;
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
