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
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionMethodStatic;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.symbols.SymbolStaticMethod;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class PartialStaticMethod implements IPartialExpression
{
	private final CodePosition position;
	private final IScopeMethod scope;
	private final IMethod method;
	private final ZenTypeFunction type;
	
	public PartialStaticMethod(CodePosition position, IScopeMethod scope, IMethod method)
	{
		this.position = position;
		this.scope = scope;
		this.method = method;
		type = new ZenTypeFunction(method.getMethodHeader());
	}

	@Override
	public Expression eval()
	{
		return new ExpressionMethodStatic(position, scope, method);
	}

	@Override
	public Expression assign(CodePosition position, Expression other)
	{
		scope.error(position, "Cannot assign to a method");
		return new ExpressionInvalid(position, scope, other.getType());
	}

	@Override
	public IPartialExpression getMember(CodePosition position, String name)
	{
		return eval().getMember(position, name);
	}

	@Override
	public List<IMethod> getMethods()
	{
		return Collections.singletonList(method);
	}

	@Override
	public IZenSymbol toSymbol()
	{
		return new SymbolStaticMethod(method);
	}

	@Override
	public ZenType getType()
	{
		return type;
	}

	@Override
	public ZenType toType(List<ZenType> genericTypes)
	{
		throw new UnsupportedOperationException("Cannot convert function to type");
	}

	@Override
	public IPartialExpression via(SymbolicFunction function)
	{
		return this;
	}
}
