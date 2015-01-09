/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
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
	
	public JavaMethodExpanding(IJavaMethod baseMethod, IModuleScope<IJavaExpression, IJavaType> scope)
	{
		this.baseMethod = baseMethod;

		MethodHeader<IJavaExpression, IJavaType> originalHeader = baseMethod.getMethodHeader();

		List<MethodParameter<IJavaExpression, IJavaType>> arguments = baseMethod.getMethodHeader().getParameters().subList(
				1,
				baseMethod.getMethodHeader().getParameters().size());
		MethodHeader<IJavaExpression, IJavaType> newHeader
				= new MethodHeader<IJavaExpression, IJavaType>(
						originalHeader.getPosition(), 
						originalHeader.getGenericParameters(),
						originalHeader.getReturnType(),
						arguments,
						originalHeader.isVarargs());

		functionType = originalHeader.getReturnType().getScope().getTypeCompiler().getFunction(scope, newHeader);
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
	public IJavaExpression callStatic(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, List<IJavaExpression> arguments)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callStaticWithConstants(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, Object... constantArguments)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callStaticNullable(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaExpression argument)
	{
		throw new UnsupportedOperationException("Not possible");
	}

	@Override
	public IJavaExpression callVirtual(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaExpression target, List<IJavaExpression> arguments)
	{
		List<IJavaExpression> newArguments = new ArrayList<IJavaExpression>();
		newArguments.add(target);
		newArguments.addAll(arguments);
		
		return baseMethod.callStatic(position, scope, newArguments);
	}

	@Override
	public IJavaExpression callVirtualWithConstants(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaExpression target, Object... constantArguments)
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

	@Override
	public void validateCall(CodePosition position, IMethodScope<IJavaExpression, IJavaType> scope, IJavaExpression... arguments)
	{
		// TODO: implement
	}
}
