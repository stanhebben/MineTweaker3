/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;

/**
 *
 * @author Stan
 * @param <E>
 */
public class InstancedMethodHeader<E extends IPartialExpression<E>>
{
	private final MethodHeader<E> header;
	private final IGenericType<E> returnType;
	private final List<MethodParameter<E>> parameters;
	
	public InstancedMethodHeader(MethodHeader<E> header, IGenericType<E> returnType, List<MethodParameter<E>> parameters)
	{
		this.header = header;
		this.returnType = returnType;
		this.parameters = parameters;
	}
	
	public InstancedMethodHeader<E> instance(TypeCapture<E> typeCapture)
	{
		List<MethodParameter<E>> newParameters = new ArrayList<>();
		for (MethodParameter<E> parameter : parameters) {
			newParameters.add(parameter.instance(typeCapture));
		}
		return new InstancedMethodHeader<>(this.header, returnType.instance(typeCapture), newParameters);
	}
	
	public MethodHeader<E> getMethodHeader()
	{
		return header;
	}

	public IGenericType<E> getReturnType()
	{
		return returnType;
	}

	public List<MethodParameter<E>> getParameters()
	{
		return parameters;
	}

	public boolean isVararg()
	{
		return header.isVarargs();
	}
	
	
	public boolean accepts(int numArguments)
	{
		if (numArguments > parameters.size())
			return isVararg();
		if (numArguments == parameters.size())
			return true;
		else {
			int checkUntil = isVararg() ? parameters.size() - 1 : parameters.size();
			for (int i = numArguments; i < checkUntil; i++) {
				if (parameters.get(i).getDefaultValue() == null)
					return false;
			}
			return true;
		}
	}
	
	public boolean acceptsWithExactTypes(List<E> arguments)
	{
		if (!accepts(arguments.size()))
			return false;

		for (int i = 0; i < arguments.size(); i++) {
			if (isVararg() && i >= this.parameters.size() - 1 && getVarArgBaseType().equals(arguments.get(i).getType()))
				continue;
			
			if (!getArgumentType(i).equals(arguments.get(i).getType()))
				return false;
		}

		return true;
	}

	public boolean accepts(IModuleScope<E> scope, List<E> arguments)
	{
		if (!accepts(arguments.size()))
			return false;

		for (int i = 0; i < arguments.size(); i++) {
			if (isVararg()
					&& i >= this.parameters.size() - 1
					&& arguments.get(i).getType().canCastImplicit(scope, getVarArgBaseType()))
				continue;
			
			if (!arguments.get(i).getType().canCastImplicit(scope, getArgumentType(i)))
				return false;
		}

		return true;
	}
	
	public boolean accepts(IModuleScope<E> scope, TypeInstance<E>... arguments)
	{
		if (!accepts(arguments.length))
			return false;
		
		for (int i = 0; i < arguments.length; i++) {
			if (isVararg()
					&& i >= this.parameters.size() - 1
					&& arguments[i].canCastImplicit(scope, getVarArgBaseType()))
				continue;
			
			if (!arguments[i].canCastImplicit(scope, getArgumentType(i)))
				return false;
		}
		
		return true;
	}

	public IGenericType<E> getVarArgBaseType()
	{
		if (!isVararg())
			return null;

		return parameters.get(parameters.size() - 1).getType().getArrayBaseType();
	}
	
	public IGenericType<E> getArgumentType(int index)
	{
		return parameters.get(index).getType();
	}
	
	public int getParameterIndex(String name)
	{
		return header.getParameterIndex(name);
	}
}
