/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.method.IMethod;

/**
 *
 * @author Stan
 */
public interface IJavaMethod extends IMethod<IJavaExpression>
{
	public String getDeclaringClass();
	
	public String getMethodName();
}
