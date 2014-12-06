/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import java.util.Map;
import java.util.Set;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ScopeConstant<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IScopeMethod<E, T>
{
	private final IScopeGlobal<E, T> scope;
	private final AccessScope access = AccessScope.createModuleScope();
	
	public ScopeConstant(IScopeGlobal<E, T> scope)
	{
		this.scope = scope;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return access;
	}

	@Override
	public ITypeCompiler<E, T> getTypes()
	{
		return scope.getTypes();
	}

	@Override
	public IZenCompileEnvironment<E, T> getEnvironment()
	{
		return scope.getEnvironment();
	}

	@Override
	public IExpressionCompiler<E, T> getExpressionCompiler()
	{
		return scope.getExpressionCompiler();
	}
	
	@Override
	public IScopeMethod<E, T> getConstantEnvironment()
	{
		return this;
	}

	@Override
	public String makeClassName()
	{
		return scope.makeClassName();
	}

	@Override
	public boolean containsClass(String name)
	{
		return scope.containsClass(name);
	}

	@Override
	public Set<String> getClassNames()
	{
		return scope.getClassNames();
	}

	@Override
	public byte[] getClass(String name)
	{
		return scope.getClass(name);
	}

	@Override
	public Map<String, byte[]> getClasses()
	{
		return scope.getClasses();
	}

	@Override
	public void putClass(String name, byte[] data)
	{
		scope.putClass(name, data);
	}

	@Override
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IScopeMethod<E, T> environment)
	{
		return environment.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
	{
		throw new UnsupportedOperationException("Not possible!");
	}

	@Override
	public boolean hasErrors()
	{
		return scope.hasErrors();
	}

	@Override
	public void error(CodePosition position, String message)
	{
		scope.error(position, message);
	}

	@Override
	public void warning(CodePosition position, String message)
	{
		scope.warning(position, message);
	}

	@Override
	public Statement<E, T> getControlStatement(String label)
	{
		return null;
	}

	@Override
	public T getReturnType()
	{
		return null;
	}
}
