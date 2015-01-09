/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.compiler.ITypeCompiler;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.java.type.TypeVariableNative;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaTypeCompiler implements ITypeCompiler<IJavaExpression, IJavaType>
{
	private final IJavaScopeGlobal scope;
	
	public JavaTypeCompiler(IJavaScopeGlobal scope)
	{
		this.scope = scope;
	}
	
	public IJavaType getNativeType(CodePosition position, Type type, TypeCapture<IJavaExpression, IJavaType> capture)
	{
		if (type instanceof ParameterizedType)
			return getParameterizedType(position, (ParameterizedType) type, capture);
		else if (type instanceof Class)
			return getNativeClassType(position, (Class) type, capture);
		else if (type instanceof TypeVariable)
			return getTypeVariableType(position, (TypeVariable) type, capture);
		else
			throw new RuntimeException("Could not convert native type: " + type);
	}
	
	private IJavaType getParameterizedType(CodePosition position, ParameterizedType type, TypeCapture<IJavaExpression, IJavaType> capture)
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
				return getNativeClassType(position, rawClass, capture);
		} else
			return getNativeType(position, raw, capture);
	}
	
	private IJavaType getTypeVariableType(CodePosition position, TypeVariable<?> type, TypeCapture<IJavaExpression, IJavaType> capture)
	{
		for (Type t : type.getBounds()) {
			return getNativeType(position, t, capture);
		}
		
		return getNativeType(position, Object.class, capture);
	}

	public IJavaType getInstancedNativeType(CodePosition position, Type type, List<IJavaType> genericTypes, TypeCapture<IJavaExpression, IJavaType> capture)
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
					TypeCapture<IJavaExpression, IJavaType> newCapture = new TypeCapture<IJavaExpression, IJavaType>(capture);
					for (int i = 0; i < parameters.length; i++) {
						newCapture.put(new TypeVariableNative(
								typeVariables[i]),
								getNativeType(position, parameters[i], capture)
						);
					}

					return getNativeClassType(position, rawCls, newCapture);
				}
			} else
				return getNativeType(position, raw, capture);
		} else if (type instanceof Class) {
			Class<?> cls = (Class) type;

			TypeVariable<?>[] typeVariables = cls.getTypeParameters();
			if (typeVariables.length != genericTypes.size()) {
				scope.getErrorLogger().errorInvalidNumberOfGenericArguments(position, getNativeType(position, cls, capture), genericTypes);
				return getNativeType(position, type, capture);
			}

			TypeCapture<IJavaExpression, IJavaType> newCapture = new TypeCapture<IJavaExpression, IJavaType>(capture);
			for (int i = 0; i < typeVariables.length; i++) {
				newCapture.put(new TypeVariableNative(typeVariables[i]), genericTypes.get(i));
			}

			return getNativeClassType(position, cls, newCapture);
		} else
			throw new RuntimeException("Could not convert native type: " + type);
	}

	// #######################
	// ### Private methods ###
	// #######################
	
	private final Map<Class<?>, IJavaType> nativeTypes = new HashMap<Class<?>, IJavaType>();
	
	private IJavaType getNativeClassType(CodePosition position, Class<?> cls, TypeCapture<IJavaExpression, IJavaType> capture)
	{
		if (nativeTypes.containsKey(cls))
			return nativeTypes.get(cls);
		else if (cls.isArray()) {
			IJavaType result = getArray(getNativeType(position, cls.getComponentType(), capture));
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

	private IJavaType getListType(ParameterizedType type)
	{
		if (type.getRawType() == List.class)
			return getArray(getNativeType(null, type.getActualTypeArguments()[0], TypeCapture.<IJavaExpression, IJavaType>empty()));

		return null;
	}

	private IJavaType getMapType(ParameterizedType type)
	{
		if (type.getRawType() == Map.class)
			return getMap(
					getNativeType(null, type.getActualTypeArguments()[1], TypeCapture.<IJavaExpression, IJavaType>empty()),
					getNativeType(null, type.getActualTypeArguments()[0], TypeCapture.<IJavaExpression, IJavaType>empty()));

		return null;
	}
	
	// ####################################
	// ### ITypeCompiler implementation ###
	// ####################################
	
	@Override
	public IJavaType getAny()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getAnyArray()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getAnyAnyMap()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getNull()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getVoid()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getBool()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getByte()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getUByte()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getShort()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getUShort()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getInt()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getUInt()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getLong()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getULong()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getFloat()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getDouble()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getChar()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getString()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getArray(IJavaType baseType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getMap(IJavaType keyType, IJavaType valueType)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IJavaType getFunction(MethodHeader<IJavaExpression, IJavaType> header)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void addExpansion(String type, TypeExpansion<IJavaExpression, IJavaType> expansion)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
