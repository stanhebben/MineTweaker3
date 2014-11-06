/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticGetter;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class SymbolStaticGetter implements IZenSymbol
{
	private final IMethod method;
	
	public SymbolStaticGetter(IMethod method)
	{
		this.method = method;
	}
	
	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod scope)
	{
		return new PartialStaticGetter(position, scope, method);
	}
}
