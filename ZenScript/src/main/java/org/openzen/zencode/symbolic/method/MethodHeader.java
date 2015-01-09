/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;
/**
 *
 * @author Stan Hebben
 * @param <E>
 * @param <T>
 */
public class MethodHeader<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 MethodHeader<ES, TS> noParameters(TS returnType)
	{
		return new MethodHeader<ES, TS>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<ES, TS>>emptyList(),
				returnType,
				Collections.<MethodParameter<ES, TS>>emptyList(),
				false);
	}
	
	public static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 MethodHeader<ES, TS> singleParameterVoid(String argumentName, TS argumentType)
	{
		return singleParameter(
				argumentType.getScope().getTypeCompiler().getVoid(argumentType.getScope()),
				argumentName,
				argumentType);
	}

	public static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 MethodHeader<ES, TS> singleParameter(
				 TS returnType,
				 String argumentName,
				 TS argumentType)
	{
		MethodParameter<ES, TS> argument = new MethodParameter<ES, TS>(
				CodePosition.SYSTEM,
				argumentName,
				argumentType,
				null);
		
		return new MethodHeader<ES, TS>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<ES, TS>>emptyList(),
				returnType,
				Collections.singletonList(argument),
				false);
	}

	private final CodePosition position;
	private final T returnType;
	private final List<GenericParameter<E, T>> genericParameters;
	private final List<MethodParameter<E, T>> parameters;
	private final boolean isVarargs;

	// optimization
	private final Map<String, Integer> parameterIndicesByName;

	public MethodHeader(
			CodePosition position,
			List<GenericParameter<E, T>> genericParameters,
			T returnType,
			List<MethodParameter<E, T>> parameters,
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
	
	public CodePosition getPosition()
	{
		return position;
	}
	
	public List<GenericParameter<E, T>> getGenericParameters()
	{
		return genericParameters;
	}

	public T getReturnType()
	{
		return returnType;
	}

	public List<MethodParameter<E, T>> getParameters()
	{
		return parameters;
	}

	public MethodParameter<E, T> getParameterByIndex(int index)
	{
		return parameters.get(index);
	}

	public MethodParameter<E, T> getParameterByName(String name)
	{
		return parameters.get(parameterIndicesByName.get(name));
	}
	
	public E makeArgumentGetExpression(int index, CodePosition position, IMethodScope<E, T> scope)
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

	public boolean accepts(int numArguments)
	{
		if (numArguments > parameters.size())
			return isVarargs;
		if (numArguments == parameters.size())
			return true;
		else {
			int checkUntil = isVarargs ? parameters.size() - 1 : parameters.size();
			for (int i = numArguments; i < checkUntil; i++) {
				if (parameters.get(i).getDefaultValue() == null)
					return false;
			}
			return true;
		}
	}
	
	public T getVarArgBaseType()
	{
		if (!isVarargs())
			return null;

		return parameters.get(parameters.size() - 1).getType().getArrayBaseType();
	}

	public boolean acceptsWithExactTypes(List<E> arguments)
	{
		if (!accepts(arguments.size()))
			return false;

		for (int i = 0; i < arguments.size(); i++) {
			if (isVarargs() && i >= this.parameters.size() - 1 && getVarArgBaseType().equals(arguments.get(i).getType()))
				continue;
			
			if (!getArgumentType(i).equals(arguments.get(i).getType()))
				return false;
		}

		return true;
	}

	public boolean accepts(List<E> arguments)
	{
		if (!accepts(arguments.size()))
			return false;

		for (int i = 0; i < arguments.size(); i++) {
			if (isVarargs()
					&& i >= this.parameters.size() - 1
					&& arguments.get(i).getType().canCastImplicit(getVarArgBaseType()))
				continue;
			
			if (!arguments.get(i).getType().canCastImplicit(getArgumentType(i)))
				return false;
		}

		return true;
	}
	
	public boolean accepts(IModuleScope<E, T> scope, T... arguments)
	{
		if (!accepts(arguments.length))
			return false;
		
		for (int i = 0; i < arguments.length; i++) {
			if (isVarargs()
					&& i >= this.parameters.size() - 1
					&& arguments[i].canCastImplicit(getVarArgBaseType()))
				continue;
			
			if (!arguments[i].canCastImplicit(getArgumentType(i)))
				return false;
		}
		
		return true;
	}

	public T getArgumentType(int index)
	{
		return parameters.get(index).getType();
	}
	
	public void complete(IMethodScope<E, T> scope)
	{
		for (MethodParameter<E, T> parameter : parameters) {
			parameter.completeContents(scope);
		}
	}
	
	public InstancedMethodHeader<E, T> instance(IDefinitionScope<E, T> scope, Map<GenericParameter<E, T>, T> genericArguments)
	{
		List<MethodParameter<E, T>> instancedParameters = new ArrayList<MethodParameter<E, T>>();
		for (MethodParameter<E, T> parameter : parameters) {
			instancedParameters.add(parameter.instance(scope, genericArguments));
		}
		
		return new InstancedMethodHeader<E, T>(
				scope, 
				returnType.instance(scope, genericArguments),
				instancedParameters, 
				isVarargs);
	}
	
	public void validate(IMethodScope<E, T> scope)
	{
		for (MethodParameter<E, T> parameter : parameters) {
			parameter.validate(scope);
		}
	}
}
