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
public final class AccessScope
{
	public static AccessScope createModuleScope()
	{
		return new AccessScope();
	}
	
	public static AccessScope createPackageScope(AccessScope moduleScope)
	{
		return new AccessScope(moduleScope);
	}
	
	public static AccessScope createClassScope(AccessScope packageScope)
	{
		return new AccessScope(packageScope.moduleScope, packageScope);
	}
	
	private final AccessScope moduleScope;
	private final AccessScope packageScope;
	
	private AccessScope()
	{
		moduleScope = this;
		packageScope = this;
	}
	
	private AccessScope(AccessScope moduleScope)
	{
		this.moduleScope = moduleScope;
		packageScope = this;
	}
	
	private AccessScope(AccessScope moduleScope, AccessScope packageScope)
	{
		this.moduleScope = moduleScope;
		this.packageScope = packageScope;
	}
	
	public boolean matchesPackage(AccessScope other)
	{
		return packageScope == other.packageScope;
	}
	
	public boolean matchesPublic(AccessScope other)
	{
		return moduleScope == other.moduleScope;
	}
	
	public boolean isAccessibleFrom(AccessScope other, int modifiers)
	{
		if ((modifiers & Modifier.PRIVATE.getCode()) > 0)
			return this == other;
		
		if ((modifiers & Modifier.PUBLIC.getCode()) > 0)
			return matchesPublic(other);
		
		if ((modifiers & Modifier.EXPORT.getCode()) > 0)
			return true;
		
		return matchesPackage(other);
	}
}
