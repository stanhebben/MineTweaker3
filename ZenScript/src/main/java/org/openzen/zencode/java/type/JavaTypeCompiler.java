/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.IZenCompileEnvironment;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaTypeCompiler
{
	private final IZenCompileEnvironment<IJavaExpression> environment;
	
	public JavaTypeCompiler(IZenCompileEnvironment<IJavaExpression> environment)
	{
		this.environment = environment;
	}
	
	public ITypeDefinition<IJavaExpression> getNativeType(CodePosition position, Type type)
	{
		if (type instanceof ParameterizedType)
			return getParameterizedType(position, (ParameterizedType) type);
		else if (type instanceof Class)
			return getNativeClassType(position, (Class) type);
		else if (type instanceof TypeVariable)
			return getTypeVariableType(position, (TypeVariable) type);
		else
			throw new RuntimeException("Could not convert native type: " + type);
	}
	
	private TypeInstance<IJavaExpression> getParameterizedType(CodePosition position, ParameterizedType type)
	{
		Type raw = type.getRawType();
		if (raw instanceof Class) {
			Class<?> rawClass = (Class) raw;
			// TODO
			//TypeVariable[] typeVariables = rawClass.getTypeParameters();
			//if (typeVariables.length != 0)
			//	environment.error(position, "Missing type parameters");

			if (List.class == rawClass)
				return getListType(type);
			else if (Map.class == rawClass)
				return getMapType(type);
			else
				return getNativeClassType(position, rawClass);
		} else
			return getNativeType(position, raw);
	}
	
	private TypeInstance<IJavaExpression> getTypeVariableType(CodePosition position, TypeVariable<?> type)
	{
		for (Type t : type.getBounds()) {
			return getNativeType(position, t);
		}
		
		return getNativeType(position, Object.class);
	}

	public TypeInstance<IJavaExpression> getInstancedNativeType(CodePosition position, Type type, List<TypeInstance<IJavaExpression>> genericTypes)
	{
		if (type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type raw = pType.getRawType();
			if (raw instanceof Class) {
				Class<?> rawCls = (Class) raw;

				if (List.class == rawCls)
					return getListType(pType);
				else if (Map.class == rawCls)
					return getMapType(pType);
				else {
					Type[] parameters = pType.getActualTypeArguments();
					TypeVariable<?>[] typeVariables = rawCls.getTypeParameters();
					TypeCapture<IJavaExpression> newCapture = new TypeCapture<IJavaExpression>(capture);
					for (int i = 0; i < parameters.length; i++) {
						newCapture.put(new TypeVariableNative<IJavaExpression>(
								typeVariables[i]),
								getNativeType(position, parameters[i], capture)
						);
					}

					return getNativeClassType(position, rawCls, newCapture);
				}
			} else
				return getNativeType(position, raw);
		} else if (type instanceof Class) {
			Class<?> cls = (Class) type;

			TypeVariable<?>[] typeVariables = cls.getTypeParameters();
			if (typeVariables.length != genericTypes.size()) {
				environment.getErrorLogger().errorInvalidNumberOfGenericArguments(position, getNativeType(position, cls), genericTypes);
				return getNativeType(position, type);
			}

			TypeCapture<IJavaExpression> newCapture = new TypeCapture<IJavaExpression>(capture);
			for (int i = 0; i < typeVariables.length; i++) {
				newCapture.put(new TypeVariableNative<IJavaExpression>(typeVariables[i]), genericTypes.get(i));
			}

			return getNativeClassType(position, cls, newCapture);
		} else
			throw new RuntimeException("Could not convert native type: " + type);
	}

	// #######################
	// ### Private methods ###
	// #######################
	
	private final Map<Class<?>, TypeInstance<IJavaExpression>> nativeTypes = new HashMap<Class<?>, TypeInstance<IJavaExpression>>();
	
	private TypeInstance<IJavaExpression> getNativeClassType(CodePosition position, Class<?> cls)
	{
		if (nativeTypes.containsKey(cls))
			return nativeTypes.get(cls);
		else if (cls.isArray()) {
			TypeInstance<IJavaExpression> result = getArray(getNativeType(position, cls.getComponentType()));
			nativeTypes.put(cls, result);
			return result;
		} else {
			//TODO
			/*IJavaType result = new ZenTypeNative(scope, cls, capture);
			nativeTypes.put(cls, result);
			result.complete(this);
			return result;*/
			return null;
		}
	}

	private ITypeDefinition<IJavaExpression> getListType(ParameterizedType type)
	{
		if (type.getRawType() == List.class)
			return getArray(getNativeType(null, type.getActualTypeArguments()[0]));

		return null;
	}

	private ITypeDefinition<IJavaExpression> getMapType(ParameterizedType type)
	{
		if (type.getRawType() == Map.class)
			return getMap(
					getNativeType(null, type.getActualTypeArguments()[1]),
					getNativeType(null, type.getActualTypeArguments()[0]));

		return null;
	}
	
	
	public JavaTypeInfo getTypeInfo(TypeInstance<IJavaExpression> type)
	{
		
	}
}
