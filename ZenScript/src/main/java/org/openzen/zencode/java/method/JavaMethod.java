/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.annotations.Named;
import org.openzen.zencode.annotations.NotNull;
import org.openzen.zencode.annotations.Optional;
import org.openzen.zencode.annotations.OptionalDouble;
import org.openzen.zencode.annotations.OptionalFloat;
import org.openzen.zencode.annotations.OptionalInt;
import org.openzen.zencode.annotations.OptionalLong;
import org.openzen.zencode.annotations.OptionalString;
import org.openzen.zencode.java.IJavaScopeGlobal;
import org.openzen.zencode.java.JavaTypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.java.expression.JavaCallStaticNullable;
import org.openzen.zencode.java.expression.JavaCallVirtual;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;
import static org.openzen.zencode.java.type.JavaTypeUtil.internal;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ExtendsGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.type.generic.ImplementsGenericParameterBound;

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

	public static IJavaMethod get(IJavaScopeGlobal scope, Class<?> cls, String name, Class<?>... parameterTypes)
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

	public static IJavaMethod get(IJavaScopeGlobal scope, Method method)
	{
		return new JavaMethod(method, scope);
	}

	public static IJavaMethod get(
			IJavaScopeGlobal scope,
			Method method,
			TypeCapture<IJavaExpression> capture)
	{
		return new JavaMethod(method, scope, capture);
	}
	
	private final Method method;
	private final TypeInstance<IJavaExpression> functionType;

	public JavaMethod(Method method, IJavaScopeGlobal scope)
	{
		this(method, scope, TypeCapture.<IJavaExpression>empty());
	}
	
	public JavaMethod(
			Method method,
			IJavaScopeGlobal scope,
			TypeCapture<IJavaExpression> capture)
	{
		this(method, scope, null, capture);
	}

	public JavaMethod(
			Method method,
			IJavaScopeGlobal scope,
			String[] argumentNames,
			TypeCapture<IJavaExpression> capture)
	{
		this.method = method;

		JavaTypeCompiler types = scope.getTypeCompiler();
		TypeInstance<IJavaExpression> returnType = types.getNativeType(null, method.getGenericReturnType(), capture);

		Type[] genericParameters = method.getGenericParameterTypes();
		List<MethodParameter<IJavaExpression>> arguments = new ArrayList<MethodParameter<IJavaExpression>>();
		for (int i = 0; i < genericParameters.length; i++) {
			TypeInstance<IJavaExpression> type = types.getNativeType(null, genericParameters[i], capture);
			String name = argumentNames == null || i >= argumentNames.length ? null : argumentNames[i];
			IJavaExpression defaultValue = null;

			IMethodScope<IJavaExpression> environment = scope.getConstantEnvironment();
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof Optional)
					defaultValue = type.createDefaultValue(null, environment);
				else if (annotation instanceof OptionalInt)
				{
					int value = ((OptionalInt) annotation).value();
					
					if (type == types.getByte())
						defaultValue = scope.getExpressionCompiler().constantByte(null, environment, (byte) value);
					else if (type == types.getUByte())
						defaultValue = scope.getExpressionCompiler().constantUByte(null, environment, value);
					else if (type == types.getShort())
						defaultValue = scope.getExpressionCompiler().constantShort(null, environment, (short) value);
					else if (type == types.getUShort())
						defaultValue = scope.getExpressionCompiler().constantUShort(null, environment, value);
					else if (type == types.getInt())
						defaultValue = scope.getExpressionCompiler().constantInt(null, environment, value);
					else if (type == types.getUInt())
						defaultValue = scope.getExpressionCompiler().constantUInt(null, environment, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalLong)
				{
					long value = ((OptionalLong) annotation).value();
					
					if (type == types.getLong())
						defaultValue = scope.getExpressionCompiler().constantLong(null, environment, value);
					else if (type == types.getULong())
						defaultValue = scope.getExpressionCompiler().constantULong(null, environment, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalFloat)
				{
					float value = ((OptionalFloat) annotation).value();
					
					if (type == types.getFloat())
						defaultValue = scope.getExpressionCompiler().constantFloat(null, environment, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalDouble)
				{
					double value = ((OptionalDouble) annotation).value();
					
					if (type == types.getDouble())
						defaultValue = scope.getExpressionCompiler().constantDouble(null, environment, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof OptionalString)
				{
					String value = ((OptionalString) annotation).value();
					
					if (type == types.getString())
						defaultValue = scope.getExpressionCompiler().constantString(null, environment, value);
					else
						throw new RuntimeException("invalid annotation on parameter in " + method.getDeclaringClass().getName() + ":" + method.getName() + " - " + annotation);
				}
				else if (annotation instanceof NotNull)
					type = type.nonNull();
				else if (annotation instanceof Named)
					name = ((Named) annotation).value();
			}

			arguments.add(new MethodParameter<IJavaExpression>(
					new CodePosition(method.getDeclaringClass().getSimpleName() + ":" + method.getName(), 0, 0),
					name,
					type,
					defaultValue));
		}
		
		
		TypeVariable<Method>[] typeVariables = method.getTypeParameters();
		List<GenericParameter<IJavaExpression>> genericMethodParameters = Collections.emptyList();
		if (typeVariables.length > 0) {
			genericMethodParameters = new ArrayList<GenericParameter<IJavaExpression>>();
			
			for (TypeVariable<Method> typeVariable : typeVariables) {
				List<IGenericParameterBound<IJavaExpression>> bounds = new ArrayList<IGenericParameterBound<IJavaExpression>>();
				for (Type typeVariableBound : typeVariable.getBounds()) {
					TypeInstance<IJavaExpression> type = types.getNativeType(CodePosition.SYSTEM, typeVariableBound, capture);
					if (type.isInterface())
						bounds.add(new ImplementsGenericParameterBound<IJavaExpression>(type));
					else
						bounds.add(new ExtendsGenericParameterBound<IJavaExpression>(type));
				}

				GenericParameter<IJavaExpression> parameter
						= new GenericParameter<IJavaExpression>(
								CodePosition.SYSTEM,
								typeVariable.getName(),
								bounds);
				genericMethodParameters.add(parameter);
			}
		}

		functionType = types.getFunction(new MethodHeader<IJavaExpression>(
				CodePosition.SYSTEM,
				genericMethodParameters,
				returnType,
				arguments,
				method.isVarArgs()));
	}

	@Override
	public boolean isStatic()
	{
		return (method.getModifiers() & Modifier.STATIC) > 0;
	}

	@Override
	public TypeInstance<IJavaExpression> getFunctionType()
	{
		return functionType;
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
	public MethodHeader<IJavaExpression> getMethodHeader()
	{
		return functionType.getFunctionHeader();
	}
	
	@Override
	public TypeInstance<IJavaExpression> getReturnType()
	{
		return functionType.getFunctionHeader().getReturnType();
	}

	@Override
	public IJavaExpression callStatic(CodePosition position, IMethodScope<IJavaExpression> scope, List<IJavaExpression> arguments)
	{
		return new JavaCallStatic(position, scope, this, arguments);
	}

	@Override
	public IJavaExpression callStaticWithConstants(CodePosition position, IMethodScope<IJavaExpression> scope, Object... constantArguments)
	{
		return new JavaCallStatic(position, scope, this, scope.getExpressionCompiler().constants(position, scope, constantArguments));
	}

	@Override
	public IJavaExpression callStaticNullable(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression argument)
	{
		return new JavaCallStaticNullable(position, scope, this, argument);
	}

	@Override
	public IJavaExpression callVirtual(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression target, List<IJavaExpression> arguments)
	{
		return new JavaCallVirtual(position, scope, this, target, arguments);
	}

	@Override
	public IJavaExpression callVirtualWithConstants(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression target, Object... constantArguments)
	{
		return new JavaCallVirtual(position, scope, this, target, scope.getExpressionCompiler().constants(position, scope, constantArguments));
	}

	@Override
	public void validateCall(CodePosition position, IMethodScope<IJavaExpression> scope, List<IJavaExpression> arguments)
	{
		MethodHeader<IJavaExpression> header = functionType.getFunctionHeader();
		if (!header.accepts(arguments.size()))
			scope.getErrorLogger().errorInvalidNumberOfArguments(position);
		
		for (int i = 0; i < arguments.size(); i++) {
			if (!arguments.get(i).getType().canCastExplicit(header.getArgumentType(i)))
				scope.getErrorLogger().errorCannotCastExplicit(position, arguments.get(i).getType(), header.getArgumentType(i));
		}
	}
}
