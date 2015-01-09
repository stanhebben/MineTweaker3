/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.packages;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class Package<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IPackageSymbol<E, T>
{
	private Map<String, IPackageSymbol<E, T>> contents;
	
	public Package()
	{
		contents = new HashMap<String, IPackageSymbol<E, T>>();
	}
	
	public Map<String, IPackageSymbol<E, T>> getAllContents()
	{
		return contents;
	}
	
	public IPackageSymbol<E, T> get(String name)
	{
		return contents.get(name);
	}

	@Override
	public IZenSymbol<E, T> toSymbol()
	{
		return null;
	}
}
