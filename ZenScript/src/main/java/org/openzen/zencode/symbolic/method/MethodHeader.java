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
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionLocalGet;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;

/**
 *
 * @author Stan Hebben
 */
public class MethodHeader
{
	public static MethodHeader noArguments(ZenType returnType)
	{
		return new MethodHeader(returnType, Collections.EMPTY_LIST, false);
	}
	
	public static MethodHeader singleArgumentVoid(String argumentName, ZenType argumentType)
	{
		return singleArgument(argumentType.getScope().getTypes().VOID, argumentName, argumentType);
	}

	public static MethodHeader singleArgument(ZenType returnType, String argumentName, ZenType argumentType)
	{
		MethodArgument argument = new MethodArgument(argumentName, argumentType, null);
		return new MethodHeader(
				returnType,
				Collections.singletonList(argument),
				false);
	}

	private final ZenType returnType;
	private final List<MethodArgument> arguments;
	private final boolean isVarargs;

	// optimization
	private final Map<String, Integer> argumentIndicesByName;

	public MethodHeader(ZenType returnType, List<MethodArgument> arguments, boolean isVarargs)
	{
		if (isVarargs && (arguments.isEmpty()))
			throw new IllegalArgumentException("Varargs method must have arguments");

		if (isVarargs && !(arguments.get(arguments.size() - 1).getType() instanceof ZenTypeArray))
			throw new IllegalArgumentException("Last varargs parameter must be an array");

		this.returnType = returnType;
		this.arguments = arguments;
		this.isVarargs = isVarargs;

		argumentIndicesByName = new HashMap<String, Integer>();
		for (int i = 0; i < arguments.size(); i++) {
			if (arguments.get(i).getName() != null)
				argumentIndicesByName.put(arguments.get(i).getName(), i);
		}
	}

	public IScopeGlobal getScope()
	{
		return returnType.getScope();
	}

	public ZenType getReturnType()
	{
		return returnType;
	}

	public List<MethodArgument> getArguments()
	{
		return arguments;
	}

	public MethodArgument getArgumentByIndex(int index)
	{
		return arguments.get(index);
	}

	public MethodArgument getArgumentByName(String name)
	{
		return arguments.get(argumentIndicesByName.get(name));
	}
	
	public Expression makeArgumentGetExpression(int index, CodePosition position, IScopeMethod scope)
	{
		return new ExpressionLocalGet(position, scope, arguments.get(index).getLocal());
	}

	public int getArgumentIndex(String name)
	{
		return argumentIndicesByName.containsKey(name) ? argumentIndicesByName.get(name) : -1;
	}

	public boolean isVarargs()
	{
		return isVarargs;
	}

	public boolean accepts(int numArguments)
	{
		if (numArguments > arguments.size())
			return isVarargs;
		if (numArguments == arguments.size())
			return true;
		else {
			int checkUntil = isVarargs ? arguments.size() - 1 : arguments.size();
			for (int i = numArguments; i < checkUntil; i++) {
				if (arguments.get(i).getDefaultValue() == null)
					return false;
			}
			return true;
		}
	}

	public ZenType getVarArgBaseType()
	{
		if (!isVarargs())
			return null;

		return ((ZenTypeArray) arguments.get(arguments.size() - 1).getType()).getBaseType();
	}

	public boolean acceptsWithExactTypes(Expression... arguments)
	{
		if (!accepts(arguments.length))
			return false;

		for (int i = 0; i < arguments.length; i++) {
			if (isVarargs() && i >= this.arguments.size() - 1 && getVarArgBaseType().equals(arguments[i].getType()))
				continue;
			
			if (!getArgumentType(i).equals(arguments[i].getType()))
				return false;
		}

		return true;
	}

	public boolean accepts(Expression... arguments)
	{
		if (!accepts(arguments.length))
			return false;

		for (int i = 0; i < arguments.length; i++) {
			if (isVarargs() && i >= this.arguments.size() - 1 && arguments[i].getType().canCastImplicit(
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

	public ZenType getArgumentType(int index)
	{
		return arguments.get(index).getType();
	}
	
	public String getSignature()
	{
		StringBuilder signature = new StringBuilder();
		signature.append('(');
		for (MethodArgument argument : arguments) {
			signature.append(argument.getType().getSignature());
		}
		signature.append(')');
		signature.append(returnType.getSignature());
		return signature.toString();
	}
}
