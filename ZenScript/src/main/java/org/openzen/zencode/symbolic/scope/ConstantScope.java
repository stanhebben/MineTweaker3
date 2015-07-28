/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.ICodeErrorLogger;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.ZenPackage;
import org.openzen.zencode.compiler.IExpressionCompiler;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ConstantScope<E extends IPartialExpression<E>>
	implements IMethodScope<E>
{
	private final IModuleScope<E> parent;
	
	public ConstantScope(IModuleScope<E> parent)
	{
		this.parent = parent;
	}
	
	@Override
	public ISymbolicDefinition<E> getDefinition()
	{
		return null;
	}

	@Override
	public AccessScope getAccessScope()
	{
		return parent.getAccessScope();
	}

	@Override
	public TypeRegistry<E> getTypeCompiler()
	{
		return parent.getTypeCompiler();
	}

	@Override
	public IZenCompileEnvironment<E> getEnvironment()
	{
		return parent.getEnvironment();
	}

	@Override
	public IExpressionCompiler<E> getExpressionCompiler()
	{
		return parent.getExpressionCompiler();
	}
	
	@Override
	public IMethodScope<E> getConstantScope()
	{
		return this;
	}

	@Override
	public IPartialExpression<E> getValue(String name, CodePosition position, IMethodScope<E> environment)
	{
		return environment.getValue(name, position, environment);
	}

	@Override
	public void putValue(String name, IZenSymbol<E> value, CodePosition position)
	{
		getErrorLogger().errorCannotAssignInConstantScope(position);
	}

	@Override
	public Statement<E> getControlStatement(String label)
	{
		return null;
	}

	@Override
	public TypeInstance<E> getReturnType()
	{
		return null;
	}

	@Override
	public ICodeErrorLogger<E> getErrorLogger()
	{
		return parent.getErrorLogger();
	}

	@Override
	public MethodHeader<E> getMethodHeader()
	{
		return null;
	}

	@Override
	public TypeCapture<E> getTypeCapture()
	{
		return TypeCapture.empty();
	}

	@Override
	public ZenPackage<E> getRootPackage()
	{
		return parent.getRootPackage();
	}

	@Override
	public IZenSymbol<E> getSymbol(String name)
	{
		return parent.getSymbol(name);
	}

	@Override
	public boolean contains(String name)
	{
		return parent.contains(name);
	}

	@Override
	public void putImport(String name, IZenSymbol<E> symbol, CodePosition position)
	{
		// nothing to do
	}

	@Override
	public boolean isConstructor()
	{
		return false;
	}

	@Override
	public IGenericType<E> getSelfType()
	{
		return null;
	}

	@Override
	public E getThis(CodePosition position, IGenericType<E> predictedType)
	{
		getErrorLogger().errorNoThisInConstant(position);
		return getExpressionCompiler().invalid(position, this);
	}
}
