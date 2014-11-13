/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.ExpressionCallVirtual;

/**
 *
 * @author Stan
 */
public abstract class AbstractMethod implements IMethod
{
	@Override
	public Expression callStatic(CodePosition position, IScopeMethod scope, Expression... values)
	{
		if (isStatic())
			return new ExpressionCallStatic(position, scope, this, values);
		
		scope.error(position, "Cannot call this method statically");
		return new ExpressionInvalid(position, scope, getReturnType());
	}
	
	@Override
	public Expression callStatic(CodePosition position, IScopeMethod scope, Object... constantArguments)
	{
		return callStatic(position, scope, Expressions.convert(position, scope, constantArguments));
	}
	
	@Override
	public Expression callVirtual(CodePosition position, IScopeMethod scope, Expression target, Expression... values)
	{
		if (!isStatic())
			return new ExpressionCallVirtual(position, scope, this, target, values);
		
		scope.error(position, "Cannot call this method virtually");
		return new ExpressionInvalid(position, scope, getReturnType());
	}
	
	@Override
	public Expression callVirtual(CodePosition position, IScopeMethod scope, Expression target, Object... constantArguments)
	{
		return callVirtual(position, scope, target, Expressions.convert(position, scope, constantArguments));
	}
	
	@Override
	public MethodHeader getMethodHeader()
	{
		return getFunctionType().getHeader();
	}
	
	@Override
	public ZenType getReturnType()
	{
		return getMethodHeader().getReturnType();
	}
}
