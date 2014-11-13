/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticField;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class SymbolStaticField implements IZenSymbol
{
	private final IField field;
	
	public SymbolStaticField(IField field)
	{
		this.field = field;
	}

	@Override
	public IPartialExpression instance(CodePosition position, IScopeMethod scope)
	{
		return new PartialStaticField(position, scope, field);
	}
}
