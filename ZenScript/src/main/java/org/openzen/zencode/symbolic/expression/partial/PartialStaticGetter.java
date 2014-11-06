/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticGetter;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class PartialStaticGetter implements IPartialExpression
{
	private final CodePosition position;
	private final IScopeMethod scope;
	private final IMethod method;
	
	public PartialStaticGetter(CodePosition position, IScopeMethod scope, IMethod method)
	{
		this.position = position;
		this.scope = scope;
		this.method = method;
	}

	@Override
	public Expression eval()
	{
		return new ExpressionCallStatic(position, scope, method);
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		scope.error(position, "Cannot assign to a static getter");
		return new ExpressionInvalid(position, scope, method.getReturnType());
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod> getMethods()
	{
		return getType().getMethods();
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return new SymbolStaticGetter(method);
	}

	@Override
	public ZenType getType()
	{
		return method.getReturnType();
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		throw new UnsupportedOperationException("Cannot convert static getter to type");
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return this;
	}
}
