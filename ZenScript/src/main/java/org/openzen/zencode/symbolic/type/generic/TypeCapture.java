/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class TypeCapture<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	@SuppressWarnings("unchecked")
	public static <E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
		 TypeCapture<E, T> empty()
	{
		return (TypeCapture<E, T>) EMPTY;
	}
	
	public static final TypeCapture EMPTY = new TypeCapture(null);
	
	private final TypeCapture<E, T> outer;
	private final Map<ITypeVariable, T> variables;

	public TypeCapture(TypeCapture<E, T> outer)
	{
		this.outer = outer;
		variables = new HashMap<ITypeVariable, T>();
	}

	public T get(ITypeVariable variable)
	{
		if (variables.containsKey(variable))
			return variables.get(variable);
		else if (outer != null)
			return outer.get(variable);
		else
			throw new RuntimeException("Could not resolve type variable");
	}

	public void put(ITypeVariable variable, T type)
	{
		variables.put(variable, type);
	}
}
