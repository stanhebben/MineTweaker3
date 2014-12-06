/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;

/**
 *
 * @author Stan
 */
public class JavaMethods
{
	private JavaMethods() {}
	
	public static String getSignature(MethodHeader<IJavaExpression, IJavaType> methodHeader)
	{
		StringBuilder result = new StringBuilder();
		result.append('(');
		
		for (MethodParameter<IJavaExpression, IJavaType> parameter : methodHeader.getParameters())
		{
			result.append(parameter.getType().getSignature());
		}
		
		result.append(')');
		result.append(methodHeader.getReturnType().getSignature());
		return result.toString();
	}
}
