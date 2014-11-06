/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionGetStaticField;
import stanhebben.zenscript.expression.ExpressionSetStaticField;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticField;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class PartialStaticField implements IPartialExpression
{
	private final CodePosition position;
	private final IScopeMethod scope;
	private final IField field;
	
	public PartialStaticField(CodePosition position, IScopeMethod scope, IField field)
	{
		this.position = position;
		this.scope = scope;
		this.field = field;
	}

	@Override
	public Expression eval()
	{
		return new ExpressionGetStaticField(position, scope, field);
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		return new ExpressionSetStaticField(position, scope, field, other);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod> getMethods()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return new SymbolStaticField(field);
	}

	@Override
	public ZenType getType()
	{
		return field.getType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		throw new UnsupportedOperationException("Cannot convert static field to type");
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return this;
	}
}
