/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.AccessScope;

/**
 *
 * @author Stan
 */
public interface IScopeModule extends IScopeGlobal
{
	public AccessScope getAccessScope();
}
