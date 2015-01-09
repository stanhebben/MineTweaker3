/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import java.util.Map;
import java.util.Set;
import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class ConstantScope<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	implements IMethodScope<E, T>
{
	private final IGlobalScope<E, T> scope;
	private final AccessScope access = AccessScope.createModuleScope();
	
	public ConstantScope(IGlobalScope<E, T> scope)
	{
		this.scope = scope;
	}
	
	@Override
	public ISymbolicDefinition<E, T> getDefinition()
	{
		return null;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return access;
	}

	@Override
	public ITypeCompiler<E, T> getTypeCompiler()
	{
		return scope.getTypeCompiler();
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
	public IMethodScope<E, T> getConstantEnvironment()
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
	public IPartialExpression<E, T> getValue(String name, CodePosition position, IMethodScope<E, T> environment)
	{
		return environment.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E, T> value, CodePosition position)
	{
		getErrorLogger().errorCannotAssignInConstantScope(position);
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

	@Override
	public ICodeErrorLogger<E, T> getErrorLogger()
	{
		return scope.getErrorLogger();
	}

	@Override
	public MethodHeader<E, T> getMethodHeader()
	{
		return null;
	}
}
