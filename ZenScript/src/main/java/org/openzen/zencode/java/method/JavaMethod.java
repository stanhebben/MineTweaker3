/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import java.lang.reflect.Method;
import java.util.List;
import org.openzen.zencode.java.JavaCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;
import org.openzen.zencode.java.util.JavaUtil;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class JavaMethod implements IJavaMethod
{
	public static final int PRIORITY_INVALID = -1;
	public static final int PRIORITY_LOW = 1;
	public static final int PRIORITY_MEDIUM = 2;
	public static final int PRIORITY_HIGH = 3;

	public static IJavaMethod get(
			JavaCompiler compiler,
			Class<?> cls,
			String name,
			Class<?>... parameterTypes)
	{
		try {
			Method method = cls.getMethod(name, parameterTypes);
			if (method == null)
				throw new RuntimeException("method " + name + " not found in class " + cls.getName());
			return new JavaMethod(method, compiler);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("method " + name + " not found in class " + cls.getName(), ex);
		} catch (SecurityException ex) {
			throw new RuntimeException("method retrieval not permitted", ex);
		}
	}
	
	private final Method method;
	private final MethodHeader<IJavaExpression> methodHeader;
	
	public JavaMethod(
			Method method,
			JavaCompiler compiler)
	{
		this(method, null, compiler);
	}

	public JavaMethod(
			Method method,
			String[] argumentNames,
			JavaCompiler compiler)
	{
		this.method = method;
		methodHeader = compiler.getMethodHeader(method, argumentNames);
	}
	
	@Override
	public String getMethodName()
	{
		return method.getName();
	}
	
	@Override
	public String getDeclaringClass()
	{
		return internal(method.getDeclaringClass());
	}

	@Override
	public String getMethodSignature()
	{
		StringBuilder result = new StringBuilder();
		result.append('(');
		for (Class<?> parameter : method.getParameterTypes())
			result.append(internal(parameter));
		
		result.append(')');
		result.append(internal(method.getReturnType()));
		
		return result.toString();
	}

	@Override
	public IGenericType<IJavaExpression> getReturnType()
	{
		return methodHeader.getReturnType();
	}

	@Override
	public MethodHeader<IJavaExpression> getHeader()
	{
		return methodHeader;
	}
	
	@Override
	public int getZCModifiers()
	{
		return JavaUtil.getZCModifiers(method.getModifiers());
	}

	@Override
	public boolean accepts(List<IJavaExpression> values)
	{
		// TODO: implement
		return true;
	}
}
