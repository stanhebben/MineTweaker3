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
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
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
		 MethodHeader<ES> noParameters(TypeInstance<ES> returnType)
	{
		return new MethodHeader<ES>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<ES>>emptyList(),
				returnType,
				Collections.<MethodParameter<ES>>emptyList(),
				false);
	}
	
	public static <ES extends IPartialExpression<ES>>
		 MethodHeader<ES> singleParameterVoid(String argumentName, TypeInstance<ES> argumentType)
	{
		return singleParameter(
				argumentType.getScope().getTypeCompiler().getVoid(argumentType.getScope()),
				argumentName,
				argumentType);
	}

	public static <ES extends IPartialExpression<ES>>
		 MethodHeader<ES> singleParameter(
				 TypeInstance<ES> returnType,
				 String argumentName,
				 TypeInstance<ES> argumentType)
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
	private final TypeInstance<E> returnType;
	private final List<GenericParameter<E>> genericParameters;
	private final List<MethodParameter<E>> parameters;
	private final boolean isVarargs;

	// optimization
	private final Map<String, Integer> parameterIndicesByName;

	public MethodHeader(
			CodePosition position,
			List<GenericParameter<E>> genericParameters,
			TypeInstance<E> returnType,
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
	
	public CodePosition getPosition()
	{
		return position;
	}
	
	public List<GenericParameter<E>> getGenericParameters()
	{
		return genericParameters;
	}

	public TypeInstance<E> getReturnType()
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
	
	public TypeInstance<E> getVarArgBaseType()
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
	
	public boolean accepts(IModuleScope<E> scope, TypeInstance<E>... arguments)
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

	public TypeInstance<E> getArgumentType(int index)
	{
		return parameters.get(index).getType();
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
	
	public InstancedMethodHeader<E> instance(IDefinitionScope<E> scope, Map<GenericParameter<E>, TypeInstance<E>> genericArguments)
	{
		TypeCapture<E> capture = new TypeCapture<E>(scope.getTypeCapture());
		for (Map.Entry<GenericParameter<E>, TypeInstance<E>> genericArgument : genericArguments.entrySet()) {
			capture.put(genericArgument.getKey(), genericArgument.getValue());
		}
		
		List<MethodParameter<E>> instancedParameters = new ArrayList<MethodParameter<E>>();
		for (MethodParameter<E> parameter : parameters) {
			instancedParameters.add(parameter.instance(scope, capture));
		}
		
		return new InstancedMethodHeader<E>(
				scope, 
				returnType.instance(scope, capture),
				instancedParameters, 
				isVarargs);
	}
	
	public void validate(IMethodScope<E> scope)
	{
		for (MethodParameter<E> parameter : parameters) {
			parameter.validate(scope);
		}
	}
}
