/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialStaticField;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class SymbolStaticField<E extends IPartialExpression<E>>
		implements IZenSymbol<E>
{
	private final IField<E> field;

	public SymbolStaticField(IField<E> field)
	{
		this.field = field;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> scope)
	{
		return new PartialStaticField<E>(position, scope, field);
	}
}
