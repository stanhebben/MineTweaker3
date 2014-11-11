/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.parser.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;

/**
 * Represents a parsed type.
 * 
 * @author Stan Hebben
 */
public interface IParsedType
{
	public ZenType compile(IScopeGlobal environment);
}
