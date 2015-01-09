/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.test.expression;

import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.test.type.TestType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public abstract class TestExpression implements IPartialExpression<TestExpression, TestType>
{
	private final CodePosition position;
	private final IMethodScope<TestExpression, TestType> scope;
	
	public TestExpression(CodePosition position, IMethodScope<TestExpression, TestType> scope)
	{
		this.position = position;
		this.scope = scope;
	}
	
	public abstract IAny getValue();
	
	@Override
	public CodePosition getPosition()
	{
		return position;
	}

	@Override
	public IMethodScope<TestExpression, TestType> getScope()
	{
		return scope;
	}

	@Override
	public TestExpression eval()
	{
		return this;
	}

	@Override
	public TestExpression assign(CodePosition position, TestExpression other)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IPartialExpression<TestExpression, TestType> getMember(CodePosition position, String name)
	{
		return getType().getInstanceMember(position, scope, this, name);
	}

	@Override
	public List<IMethod<TestExpression, TestType>> getMethods()
	{
		return getType().getInstanceMethods();
	}

	@Override
	public IPartialExpression<TestExpression, TestType> call(CodePosition position, IMethod<TestExpression, TestType> method, TestExpression... arguments)
	{
		return method.callVirtual(position, scope, this, arguments);
	}

	@Override
	public TestExpression cast(CodePosition position, TestType type)
	{
		return getType().getCastingRule(getScope().getAccessScope(), type).cast(position, scope, this);
	}

	@Override
	public IZenSymbol<TestExpression, TestType> toSymbol()
	{
		throw new UnsupportedOperationException("Not a symbol");
	}

	@Override
	public TestType toType(List<TestType> genericTypes)
	{
		throw new UnsupportedOperationException("Not a type");
	}

	@Override
	public IPartialExpression<TestExpression, TestType> via(SymbolicFunction<TestExpression, TestType> function)
	{
		return this;
	}
}
