/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker;

import stanhebben.zenscript.compiler.IScopeGlobal;

/**
 *
 * @author Stan
 */
public interface IBracketHandlerFactory {
	public IBracketHandler construct(IScopeGlobal scope);
}
