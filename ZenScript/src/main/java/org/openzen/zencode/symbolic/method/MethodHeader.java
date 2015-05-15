/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;
/**
 *
 * @author Stan Hebben
 * @param <E>
 */
public class MethodHeader<E extends IPartialExpression<E>>
{
	public static <ES extends IPartialExpression<ES>>
		 MethodHeader<ES> noParameters(IGenericType<ES> returnType)
	{
		return new MethodHeader<ES>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<ES>>emptyList(),
				returnType,
				Collections.<MethodParameter<ES>>emptyList(),
				false);
	}
	
	public static <ES extends IPartialExpression<ES>>
		 MethodHeader<ES> singleParameterVoid(IModuleScope<ES> scope, String argumentName, IGenericType<ES> argumentType)
	{
		return singleParameter(
				scope.getTypeCompiler().void_,
				argumentName,
				argumentType);
	}

	public static <ES extends IPartialExpression<ES>>
		 MethodHeader<ES> singleParameter(
				 IGenericType<ES> returnType,
				 String argumentName,
				 IGenericType<ES> argumentType)
	{
		MethodParameter<ES> argument = new MethodParameter<ES>(
				CodePosition.SYSTEM,
				argumentName,
				argumentType,
				null);
		
		return new MethodHeader<ES>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<ES>>emptyList(),
				returnType,
				Collections.singletonList(argument),
				false);
	}

	private final CodePosition position;
	private final IGenericType<E> returnType;
	private final List<GenericParameter<E>> genericParameters;
	private final List<MethodParameter<E>> parameters;
	private final boolean isVarargs;

	// optimization
	private final Map<String, Integer> parameterIndicesByName;

	public MethodHeader(
			CodePosition position,
			List<GenericParameter<E>> genericParameters,
			IGenericType<E> returnType,
			List<MethodParameter<E>> parameters,
			boolean isVarargs)
	{
		if (isVarargs && (parameters.isEmpty()))
			throw new IllegalArgumentException("Varargs method must have arguments");

		if (isVarargs && (parameters.get(parameters.size() - 1).getType()).getArrayBaseType() == null)
			throw new IllegalArgumentException("Last varargs parameter must be an array");
		
		this.position = position;
		this.genericParameters = genericParameters;
		this.returnType = returnType;
		this.parameters = parameters;
		this.isVarargs = isVarargs;

		parameterIndicesByName = new HashMap<String, Integer>();
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getName() != null)
				parameterIndicesByName.put(parameters.get(i).getName(), i);
		}
	}
	
	public InstancedMethodHeader<E> instance(TypeInstance<E> instance)
	{
		return instance(instance.getTypeCapture());
	}
	
	public InstancedMethodHeader<E> instance()
	{
		return instance(TypeCapture.empty());
	}
	
	public InstancedMethodHeader<E> instance(TypeCapture<E> typeCapture)
	{
		IGenericType<E> instancedReturnType = returnType.instance(typeCapture);
		List<MethodParameter<E>> methodParameters = new ArrayList<>();
		for (MethodParameter<E> methodParameter : parameters)
		{
			methodParameters.add(methodParameter.instance(typeCapture));
		}
		
		return new InstancedMethodHeader<>(this, instancedReturnType, methodParameters);
	}
	
	public CodePosition getPosition()
	{
		return position;
	}
	
	public List<GenericParameter<E>> getGenericParameters()
	{
		return genericParameters;
	}

	public IGenericType<E> getReturnType()
	{
		return returnType;
	}

	public List<MethodParameter<E>> getParameters()
	{
		return parameters;
	}
	
	public MethodParameter<E> getParameterByIndex(int index)
	{
		return parameters.get(index);
	}

	public MethodParameter<E> getParameterByName(String name)
	{
		return parameters.get(parameterIndicesByName.get(name));
	}
	
	public E makeArgumentGetExpression(int index, CodePosition position, IMethodScope<E> scope)
	{
		return scope.getExpressionCompiler().localGet(position, scope, parameters.get(index).getLocal());
	}

	public int getParameterIndex(String name)
	{
		return parameterIndicesByName.containsKey(name) ? parameterIndicesByName.get(name) : -1;
	}

	public boolean isVarargs()
	{
		return isVarargs;
	}

	public void completeMembers(IModuleScope<E> scope)
	{
		for (GenericParameter<E> genericParameter : genericParameters) {
			genericParameter.completeMembers(scope);
		}
	}
	
	public void completeContents(IMethodScope<E> scope)
	{
		for (MethodParameter<E> parameter : parameters) {
			parameter.completeContents(scope);
		}
	}
	
	public void validate(IMethodScope<E> scope)
	{
		for (MethodParameter<E> parameter : parameters) {
			parameter.validate(scope);
		}
	}
	
	public MethodHeader<E> withoutFirstParameter()
	{
		List<MethodParameter<E>> trimmedParameters = parameters.subList(1, parameters.size());
		return new MethodHeader<>(position, genericParameters, returnType, trimmedParameters, isVarargs);
	}
}
