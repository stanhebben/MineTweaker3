/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenGetter;
import org.openzen.zencode.annotations.ZenMethod;
import org.openzen.zencode.annotations.ZenMethodStatic;
import org.openzen.zencode.annotations.ZenOperator;
import org.openzen.zencode.annotations.ZenSetter;
import org.openzen.zencode.compiler.TypeRegistry;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.member.JavaNativeCaster;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.TypeDefinition;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaTypeCompiler
{
	private IModuleScope<IJavaExpression> scope;
	private final Map<Class<?>, NativeTypeData> nativeTypes = new HashMap<>();
	private final Set<Class<?>> nonNullTypes = new HashSet<>();
	
	public JavaTypeCompiler()
	{
	}
	
	public void init(IModuleScope<IJavaExpression> scope)
	{
		this.scope = scope;
		
		TypeRegistry<IJavaExpression> types = scope.getTypeCompiler();
		nativeTypes.put(void.class, new NativeTypeData(false, types.voidDefinition));
		nativeTypes.put(boolean.class, new NativeTypeData(false, types.boolDefinition));
		nativeTypes.put(byte.class, new NativeTypeData(false, types.byteDefinition));
		nativeTypes.put(short.class, new NativeTypeData(false, types.shortDefinition));
		nativeTypes.put(int.class, new NativeTypeData(false, types.intDefinition));
		nativeTypes.put(long.class, new NativeTypeData(false, types.longDefinition));
		nativeTypes.put(float.class, new NativeTypeData(false, types.floatDefinition));
		nativeTypes.put(double.class, new NativeTypeData(false, types.doubleDefinition));
		nativeTypes.put(char.class, new NativeTypeData(false, types.charDefinition));
		nativeTypes.put(Boolean.class, new NativeTypeData(true, types.boolDefinition));
		nativeTypes.put(Byte.class, new NativeTypeData(true, types.byteDefinition));
		nativeTypes.put(Short.class, new NativeTypeData(true, types.shortDefinition));
		nativeTypes.put(Integer.class, new NativeTypeData(true, types.intDefinition));
		nativeTypes.put(Long.class, new NativeTypeData(true, types.longDefinition));
		nativeTypes.put(Float.class, new NativeTypeData(true, types.floatDefinition));
		nativeTypes.put(Double.class, new NativeTypeData(true, types.doubleDefinition));
		nativeTypes.put(Character.class, new NativeTypeData(true, types.charDefinition));
		nativeTypes.put(String.class, new NativeTypeData(true, types.stringDefinition));
	}
	
	public IGenericType<IJavaExpression> getNativeType(CodePosition position, Type type)
	{
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			if (parameterizedType.getRawType() == List.class)
				return getListType(parameterizedType);
			else if (parameterizedType.getRawType() == Map.class)
				return getMapType(parameterizedType);
			
			return getParameterizedType(position, parameterizedType);
		} else if (type instanceof Class) {
			NativeTypeData typeInfo = getNativeClassType(position, (Class) type);
			return new TypeInstance<>(typeInfo.definition, TypeCapture.empty(), typeInfo.nullable);
		} else if (type instanceof TypeVariable)
			return getTypeVariableType(position, (TypeVariable) type);
		else
			throw new RuntimeException("Could not convert native type: " + type);
	}
	
	public ITypeDefinition<IJavaExpression> getNativeType(CodePosition position, Class<?> cls)
	{
		TypeDefinition<IJavaExpression> typeDefinition = new TypeDefinition<>();
		
		for (Method method : cls.getMethods()) {
			String methodName = method.getName();
			
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof ZenCaster) {
					if (method.getParameterTypes().length != 0)
						throw new RuntimeException("Casters cannot have parameters");
					
					return new JavaNativeCaster(new JavaMethod(method, compiler));
				} else if (annotation instanceof ZenGetter) {
					ZenGetter getterAnnotation = (ZenGetter) annotation;
					String name = getterAnnotation.value().length() == 0 ? method.getName() : getterAnnotation.value();
					
					if (method.getParameterTypes().length != 0)
						throw new RuntimeException("Getter cannot have parameters");
					
					return new JavaNativeGetter(method, name);
				} else if (annotation instanceof ZenSetter) {
					ZenSetter setterAnnotation = (ZenSetter) annotation;
					String name = setterAnnotation.value().length() == 0 ? method.getName() : setterAnnotation.value();

					if (method.getParameterTypes().length != 1)
						throw new RuntimeException("Setter cannot have parameters");
					
					return new JavaNativeSetter(method, name);
				} else if (annotation instanceof ZenOperator) {
					ZenOperator operatorAnnotation = (ZenOperator) annotation;

					OperatorType operator = operatorAnnotation.value();
					if (method.getParameterTypes().length + 1 != operator.getArgumentCount())
						throw new RuntimeException("Operator has the wrong number of parameters");
					
					return new JavaNativeOperator(method, name);
				} else if (annotation instanceof ZenMethod) {
					ZenMethod methodAnnotation = (ZenMethod) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();
					
					return new JavaNativeMethod(method, methodName);
				} else if (annotation instanceof ZenMethodStatic) {
					ZenMethodStatic methodAnnotation = (ZenMethodStatic) annotation;
					if (methodAnnotation.value().length() > 0)
						methodName = methodAnnotation.value();
					
					return new JavaNativeMethod(method, methodName);
				}
			}
		}
		
		return typeDefinition;
	}
	
	private IGenericType<IJavaExpression> getParameterizedType(CodePosition position, ParameterizedType type)
	{
		Type raw = type.getRawType();
		
		if (raw instanceof Class) {
			Type[] parameters = type.getActualTypeArguments();
			Class<?> rawClass = (Class) raw;
			
			NativeTypeData info = getNativeClassType(position, rawClass);
			List<? extends ITypeVariable<IJavaExpression>> typeVariables = info.definition.getGenericParameters();
			
			TypeCapture<IJavaExpression> typeCapture;
			if (parameters.length == 0) {
				typeCapture = TypeCapture.empty();
			} else {
				typeCapture = new TypeCapture<>(null);
				for (int i = 0; i < parameters.length; i++) {
					typeCapture.put(typeVariables.get(i), getNativeType(position, parameters[i]));
				}
			}
			
			return new TypeInstance<>(info.definition, typeCapture, info.nullable);
		} else
			return getNativeType(position, raw);
	}
	
	private IGenericType<IJavaExpression> getTypeVariableType(CodePosition position, TypeVariable<?> type)
	{
		for (Type t : type.getBounds()) {
			return getNativeType(position, t);
		}
		
		return getNativeType(position, (Type) Object.class);
	}

	// #######################
	// ### Private methods ###
	// #######################
	
	private NativeTypeData getNativeClassType(CodePosition position, Class<?> cls)
	{
		if (!nativeTypes.containsKey(cls))
		{
			if (cls.isArray()) {
				ITypeDefinition<IJavaExpression> base = getNativeClassType(position, cls.getComponentType()).definition;
				ITypeDefinition<IJavaExpression> result = scope.getTypeCompiler().arrayDefinition;
				nativeTypes.put(cls, new NativeTypeData(true, result));
			} else {
				ITypeDefinition<IJavaExpression> result = new JavaTypeDefinition(cls);
				nativeTypes.put(cls, new NativeTypeData(true, result));
			}
		}
		
		return nativeTypes.get(cls);
	}

	private IGenericType<IJavaExpression> getListType(ParameterizedType type)
	{
		if (type.getRawType() == List.class)
			return scope.getTypeCompiler().getArray(getNativeType(null, type.getActualTypeArguments()[0]));
		
		return null;
	}

	private IGenericType<IJavaExpression> getMapType(ParameterizedType type)
	{
		if (type.getRawType() == Map.class)
			return scope.getTypeCompiler().getMap(
					getNativeType(null, type.getActualTypeArguments()[1]),
					getNativeType(null, type.getActualTypeArguments()[0]));

		return null;
	}
	
	private class NativeTypeData
	{
		private final boolean nullable;
		private final ITypeDefinition<IJavaExpression> definition;
		
		public NativeTypeData(boolean nullable, ITypeDefinition<IJavaExpression> definition)
		{
			this.nullable = nullable;
			this.definition = definition;
		}
	}
}
