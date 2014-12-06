/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.method;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
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
		return new MethodHeader<ES, TS>(returnType, Collections.<MethodParameter<ES, TS>>emptyList(), false);
	}
	
	public static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 MethodHeader<ES, TS> singleParameterVoid(String argumentName, TS argumentType)
	{
		return singleParameter(argumentType.getScope().getTypes().getVoid(), argumentName, argumentType);
	}

	public static <ES extends IPartialExpression<ES, TS>, TS extends IZenType<ES, TS>>
		 MethodHeader<ES, TS> singleParameter(TS returnType, String argumentName, TS argumentType)
	{
		MethodParameter<ES, TS> argument = new MethodParameter<ES, TS>(argumentName, argumentType, null);
		return new MethodHeader<ES, TS>(
				returnType,
				Collections.singletonList(argument),
				false);
	}

	private final T returnType;
	private final List<MethodParameter<E, T>> parameters;
	private final boolean isVarargs;

	// optimization
	private final Map<String, Integer> parameterIndicesByName;

	public MethodHeader(T returnType, List<MethodParameter<E, T>> parameters, boolean isVarargs)
	{
		if (isVarargs && (parameters.isEmpty()))
			throw new IllegalArgumentException("Varargs method must have arguments");

		if (isVarargs && (parameters.get(parameters.size() - 1).getType()).getArrayBaseType() == null)
			throw new IllegalArgumentException("Last varargs parameter must be an array");

		this.returnType = returnType;
		this.parameters = parameters;
		this.isVarargs = isVarargs;

		parameterIndicesByName = new HashMap<String, Integer>();
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getName() != null)
				parameterIndicesByName.put(parameters.get(i).getName(), i);
		}
	}

	public IScopeGlobal<E, T> getScope()
	{
		return returnType.getScope();
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
	
	public E makeArgumentGetExpression(int index, CodePosition position, IScopeMethod<E, T> scope)
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

	public boolean acceptsWithExactTypes(E... arguments)
	{
		if (!accepts(arguments.length))
			return false;

		for (int i = 0; i < arguments.length; i++) {
			if (isVarargs() && i >= this.parameters.size() - 1 && getVarArgBaseType().equals(arguments[i].getType()))
				continue;
			
			if (!getArgumentType(i).equals(arguments[i].getType()))
				return false;
		}

		return true;
	}

	public boolean accepts(E... arguments)
	{
		if (!accepts(arguments.length))
			return false;

		for (int i = 0; i < arguments.length; i++) {
			if (isVarargs() && i >= this.parameters.size() - 1 && arguments[i].getType().canCastImplicit(
					arguments[i].getScope().getAccessScope(),
					getVarArgBaseType()))
				continue;
			
			if (!arguments[i].getType().canCastImplicit(
					arguments[i].getScope().getAccessScope(),
					getArgumentType(i)))
				return false;
		}

		return true;
	}

	public T getArgumentType(int index)
	{
		return parameters.get(index).getType();
	}
}
