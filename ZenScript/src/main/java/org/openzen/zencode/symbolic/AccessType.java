/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic;

/**
 *
 * @author Stan
 */
public enum AccessType
{
	PRIVATE,
	PUBLIC,
	EXPORT;
	
	public boolean isVisible(AccessScope usingScope, AccessScope definingScope)
	{
		switch (this) {
			case PRIVATE: return usingScope == definingScope;
			case PUBLIC: return usingScope.matchesPublic(usingScope);
			case EXPORT: return true;
			default: return false;
		}
	}
}
