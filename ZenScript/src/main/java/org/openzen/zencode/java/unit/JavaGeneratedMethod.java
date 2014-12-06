/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.unit;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaGeneratedMethod implements IJavaMethod
{
	private int modifiers;
	
	
	@Override
	public String getDeclaringClass()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getMethodName()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callStatic(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression... arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callStaticWithConstants(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, Object... constantArguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callStaticNullable(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression argument)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callVirtual(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression target, IJavaExpression... arguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaExpression callVirtualWithConstants(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression target, Object... constantArguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isStatic()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getFunctionType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public MethodHeader<IJavaExpression, IJavaType> getMethodHeader()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getReturnType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
