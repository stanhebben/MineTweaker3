/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;

/**
 * An expanding method is a static method that acts as a virtual method for an
 * existing class. The "this" parameter becomes the first parameter for the
 * static method.
 *
 * @author Stan Hebben
 */
public class JavaMethodExpanding implements IJavaMethod
{
	private final IJavaMethod baseMethod;
	private final IJavaType functionType;

	public JavaMethodExpanding(IJavaMethod baseMethod)
	{
		this.baseMethod = baseMethod;

		MethodHeader<IJavaExpression, IJavaType> originalHeader = baseMethod.getMethodHeader();

		List<MethodParameter<IJavaExpression, IJavaType>> arguments = baseMethod.getMethodHeader().getParameters().subList(
				1,
				baseMethod.getMethodHeader().getParameters().size());
		MethodHeader<IJavaExpression, IJavaType> newHeader
				= new MethodHeader<IJavaExpression, IJavaType>(originalHeader.getReturnType(), arguments, originalHeader.isVarargs());

		functionType = originalHeader.getReturnType().getScope().getTypes().getFunction(newHeader);
	}

	@Override
	public boolean isStatic()
	{
		return false;
	}

	@Override
	public IJavaType getFunctionType()
	{
		return functionType;
	}

	@Override
	public String getDeclaringClass()
	{
		return baseMethod.getDeclaringClass();
	}

	@Override
	public String getMethodName()
	{
		return baseMethod.getMethodName();
	}

	@Override
	public IJavaExpression callStatic(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression... arguments)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callStaticWithConstants(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, Object... constantArguments)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callStaticNullable(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression argument)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callVirtual(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression target, IJavaExpression... arguments)
	{
		IJavaExpression[] newArguments = new IJavaExpression[arguments.length + 1];
		System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
		newArguments[0] = target;
		
		return baseMethod.callStatic(position, scope, newArguments);
	}

	@Override
	public IJavaExpression callVirtualWithConstants(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression target, Object... constantArguments)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public MethodHeader<IJavaExpression, IJavaType> getMethodHeader()
	{
		return functionType.getFunctionHeader();
	}

	@Override
	public IJavaType getReturnType()
	{
		return functionType.getFunctionHeader().getReturnType();
	}
}
