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

/**
 *
 * @author Stan
 * @param <E>
 */
public class Package<E extends IPartialExpression<E>>
	implements IPackageSymbol<E>
{
	private Map<String, IPackageSymbol<E>> contents;
	
	public Package()
	{
		contents = new HashMap<String, IPackageSymbol<E>>();
	}
	
	public Map<String, IPackageSymbol<E>> getAllContents()
	{
		return contents;
	}
	
	public IPackageSymbol<E> get(String name)
	{
		return contents.get(name);
	}

	@Override
	public IZenSymbol<E> toSymbol()
	{
		return null;
	}
}
