/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java;

import org.openzen.zencode.java.method.JavaMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openzen.zencode.symbolic.field.IField;
import org.openzen.zencode.symbolic.field.JavaField;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolStaticField;
import org.openzen.zencode.symbolic.symbols.SymbolStaticMethod;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 */
public class JavaNative
{
	private JavaNative() {}
	
	public static IMethod getStaticMethod(IScopeGlobal scope, Class<?> cls, String name, Class... parameterTypes)
	{
		try {
			Method method = cls.getMethod(name, parameterTypes);
			if (method == null)
				throw new RuntimeException("method " + name + " not found in class " + cls.getName());
			return new JavaMethod(method, scope.getTypes(), TypeCapture.EMPTY);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("method " + name + " not found in class " + cls.getName(), ex);
		} catch (SecurityException ex) {
			throw new RuntimeException("method retrieval not permitted", ex);
		}
	}
	
	public static IMethod getStaticMethod(IScopeGlobal scope, Method method)
	{
		return new JavaMethod(method, scope.getTypes(), TypeCapture.EMPTY);
	}
	
	public static IZenSymbol getStaticMethodSymbol(IScopeGlobal scope, Class<?> cls, String name, Class... parameterTypes)
	{
		return new SymbolStaticMethod(getStaticMethod(scope, cls, name, parameterTypes));
	}
	
	public static IZenSymbol getStaticMethodSymbol(IScopeGlobal scope, Method method)
	{
		return new SymbolStaticMethod(getStaticMethod(scope, method));
	}
	
	public static IField getStaticField(IScopeGlobal scope, Class<?> cls, String name)
	{
		try {
			Field field = cls.getField(name);
			return new JavaField(field, scope.getTypes());
		} catch (NoSuchFieldException ex) {
			Logger.getLogger(JavaNative.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} catch (SecurityException ex) {
			Logger.getLogger(JavaNative.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}
	
	public static IField getStaticField(IScopeGlobal scope, Field field)
	{
		return new JavaField(field, scope.getTypes());
	}
	
	public static IZenSymbol getStaticFieldSymbol(IScopeGlobal scope, Class<?> cls, String name)
	{
		return new SymbolStaticField(getStaticField(scope, cls, name));
	}
	
	public static IZenSymbol getStaticFieldSymbol(IScopeGlobal scope, Field field)
	{
		return new SymbolStaticField(getStaticField(scope, field));
	}
}
