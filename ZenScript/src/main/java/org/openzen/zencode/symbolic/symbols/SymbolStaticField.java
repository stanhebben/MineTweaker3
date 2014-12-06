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
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class SymbolStaticField<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
		implements IZenSymbol<E, T>
{
	private final IField<E, T> field;

	public SymbolStaticField(IField<E, T> field)
	{
		this.field = field;
	}

	@Override
	public IPartialExpression<E, T> instance(CodePosition position, IScopeMethod<E, T> scope)
	{
		return new PartialStaticField<E, T>(position, scope, field);
	}
}
