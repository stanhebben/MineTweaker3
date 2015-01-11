/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.lang.annotation.Annotation;
import org.openzen.zencode.java.field.JavaField;
import org.openzen.zencode.java.method.JavaMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenMethodStatic;
import org.openzen.zencode.annotations.ZenOperator;
import org.openzen.zencode.annotations.ZenSetter;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.field.IJavaField;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.java.method.JavaMethodExpanding;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.StaticFieldSymbol;
import org.openzen.zencode.symbolic.symbols.StaticMethodSymbol;
import org.openzen.zencode.symbolic.type.TypeExpansion;

/**
 *
 * @author Stan
 */
public class JavaNative
{
	private JavaNative()
	{
	}

	public static IJavaMethod getStaticMethod(IModuleScope<IJavaExpression> scope, Class<?> cls, String name, Class<?>... parameterTypes)
	{
		try {
			Method method = cls.getMethod(name, parameterTypes);
			if (method == null)
				throw new RuntimeException("method " + name + " not found in class " + cls.getName());
			return new JavaMethod(method, scope);
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("method " + name + " not found in class " + cls.getName(), ex);
		} catch (SecurityException ex) {
			throw new RuntimeException("method retrieval not permitted", ex);
		}
	}

	public static IJavaMethod getStaticMethod(IJavaScopeGlobal scope, Method method)
	{
		return new JavaMethod(method, scope);
	}

	public static IZenSymbol<IJavaExpression> getStaticMethodSymbol(IJavaScopeGlobal scope, Class<?> cls, String name, Class<?>... parameterTypes)
	{
		return new StaticMethodSymbol<IJavaExpression>(getStaticMethod(scope, cls, name, parameterTypes));
	}

	public static IZenSymbol<IJavaExpression> getStaticMethodSymbol(IJavaScopeGlobal scope, Method method)
	{
		return new StaticMethodSymbol<IJavaExpression>(getStaticMethod(scope, method));
	}

	public static IJavaField getStaticField(IJavaScopeGlobal scope, Class<?> cls, String name)
	{
		try {
			Field field = cls.getField(name);
			return new JavaField(field, scope.getTypeCompiler());
		} catch (NoSuchFieldException ex) {
			Logger.getLogger(JavaNative.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} catch (SecurityException ex) {
			Logger.getLogger(JavaNative.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static IJavaField getStaticField(IModuleScope<IJavaExpression> scope, Field field)
	{
		return new JavaField(field, scope.getTypeCompiler());
	}

	public static IZenSymbol<IJavaExpression> getStaticFieldSymbol(IModuleScope<IJavaExpression> scope, Class<?> cls, String name)
	{
		return new StaticFieldSymbol<IJavaExpression>(getStaticField(scope, cls, name));
	}

	public static IZenSymbol<IJavaExpression> getStaticFieldSymbol(IModuleScope<IJavaExpression> scope, Field field)
	{
		return new StaticFieldSymbol<IJavaExpression>(getStaticField(scope, field));
	}

	public static void addExpansion(IModuleScope<IJavaExpression> scope, TypeExpansion<IJavaExpression> expansion, Class<?> annotatedClass)
	{
		for (Method method : annotatedClass.getMethods()) {
			String methodName = method.getName();

			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof ZenCaster) {
					checkStatic(method);
					expansion.addCaster(JavaMethod.get(scope, method));
				} else if (annotation instanceof ZenGetter) {
					checkStatic(method);
					ZenGetter getterAnnotation = (ZenGetter) annotation;
					String name = getterAnnotation.value().length() == 0 ? method.getName() : getterAnnotation.value();

					if (method.getParameterTypes().length == 0)
						expansion.addStaticGetter(name, JavaMethod.get(scope, method));
					else if (method.getParameterTypes().length == 1)
						expansion.addGetter(name, JavaMethod.get(scope, method));
					else
						throw new IllegalArgumentException("Not a valid getter - too many parameters");
				} else if (annotation instanceof ZenSetter) {
					checkStatic(method);
					ZenSetter setterAnnotation = (ZenSetter) annotation;
					String name = setterAnnotation.value().length() == 0 ? method.getName() : setterAnnotation.value();

					if (method.getParameterTypes().length == 1)
						expansion.addStaticSetter(name, JavaMethod.get(scope, method));
					else if (method.getParameterTypes().length == 2)
						expansion.addSetter(name, JavaMethod.get(scope, method));
					else
						throw new IllegalArgumentException("Not a valid setter - must have 1 or 2 parameters");
				} else if (annotation instanceof ZenOperator) {
					checkStatic(method);
					ZenOperator operatorAnnotation = (ZenOperator) annotation;

					OperatorType operator = operatorAnnotation.value();
					if (operator.getArgumentCount() != method.getParameterTypes().length)
						throw new RuntimeException("Numbor of operator arguments is incorrect");

					expansion.addOperator(operator, JavaMethod.get(scope, method));
				} else if (annotation instanceof ZenMethod) {
					checkStatic(method);
					ZenMethod methodAnnotation = (ZenMethod) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();

					expansion.addMethod(methodName, new JavaMethodExpanding(JavaMethod.get(scope, method), scope));
				} else if (annotation instanceof ZenMethodStatic) {
					checkStatic(method);
					ZenMethodStatic methodAnnotation = (ZenMethodStatic) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();

					expansion.addStaticMethod(methodName, JavaMethod.get(scope, method));
				}
			}
		}
	}

	/**
	 * Checks if the given method is static. Throws an exception if not.
	 *
	 * @param method metod to validate
	 */
	private static void checkStatic(Method method)
	{
		if ((method.getModifiers() & Modifier.STATIC) == 0)
			throw new RuntimeException("Expansion method " + method.getName() + " must be static");
	}
}
