/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;

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
	public ZenType compile(IScopeGlobal scope) {
		return new ZenTypeAssociative(
				valueType.compile(scope),
				keyType.compile(scope));
	}
	
	@Override
	public String toString()
	{
		return valueType + "[" + keyType + "]";
	}
}
