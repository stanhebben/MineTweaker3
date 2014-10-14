/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package zenscript.symbolic.method;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;

/**
 *
 * @author Stan
 */
public class MethodHeader
{
	private final ZenType returnType;
	private final List<MethodArgument> arguments;
	private final boolean isVarargs;
	
	// optimization
	private final Map<String, Integer> argumentIndicesByName;
	
	public MethodHeader(ZenType returnType, List<MethodArgument> arguments, boolean isVarargs)
	{
		if (isVarargs && (arguments.isEmpty()))
			throw new IllegalArgumentException("Varargs method must have arguments");
		
		if (isVarargs && !(arguments.get(0).getType() instanceof ZenTypeArray))
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
	
	public ZenType getReturnType() {
		return returnType;
	}
	
	public List<MethodArgument> getArguments() {
		return arguments;
	}
	
	public MethodArgument getArgumentByIndex(int index) {
		return arguments.get(index);
	}
	
	public MethodArgument getArgumentByName(String name) {
		return arguments.get(argumentIndicesByName.get(name));
	}
	
	public int getArgumentIndex(String name) {
		return argumentIndicesByName.containsKey(name) ? argumentIndicesByName.get(name) : -1;
	}
	
	public boolean isVarargs() {
		return isVarargs;
	}
	
	public boolean accepts(int numArguments)
	{
		if (numArguments > arguments.size())
			return isVarargs;
		if (numArguments == arguments.size())
			return true;
		else {
			for (int i = numArguments; i < arguments.size(); i++) {
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
			if (!arguments[i].getType().canCastImplicit(getArgumentType(i)))
				return false;
		}
		
		return true;
	}
	
	public ZenType getArgumentType(int index)
	{
		return arguments.get(index).getType();
	}
}
