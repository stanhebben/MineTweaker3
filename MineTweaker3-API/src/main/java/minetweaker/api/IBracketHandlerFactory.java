/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;

/**
 * Bracket handler factories generate bracket handlers for a specific
 * compilation environment.
 *
 * API Status: Frozen
 *
 * @author Stan Hebben
 */
public interface IBracketHandlerFactory
{
	public IBracketHandler construct(IScopeGlobal scope);
}
