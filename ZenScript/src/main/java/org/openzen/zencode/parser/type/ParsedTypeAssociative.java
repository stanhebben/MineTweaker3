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
 * Parsed associative array (valuetype[keytype]). Same as a dictionary or
 * hashmap.
 * 
 * @author Stan Hebben
 */
public class ParsedTypeAssociative implements IParsedType {
	private final IParsedType keyType;
	private final IParsedType valueType;
	
	public ParsedTypeAssociative(IParsedType keyType, IParsedType valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@Override
	public <E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		 T compile(IScopeGlobal<E, T> scope)
	{
		return scope.getTypes().getMap(keyType.compile(scope), valueType.compile(scope));
	}
	
	@Override
	public String toString()
	{
		return valueType + "[" + keyType + "]";
	}
}
