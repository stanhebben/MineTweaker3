/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.HashMap;
import java.util.Map;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 */
public class TypeCapture<E extends IPartialExpression<E>>
{
	@SuppressWarnings("unchecked")
	public static <E extends IPartialExpression<E>>
		 TypeCapture<E> empty()
	{
		return (TypeCapture<E>) EMPTY;
	}
	
	private static final TypeCapture EMPTY = new TypeCapture(null);
	
	private final TypeCapture<E> outer;
	private final Map<ITypeVariable<E>, TypeInstance<E>> variables;

	public TypeCapture(TypeCapture<E> outer)
	{
		this.outer = outer;
		variables = new HashMap<ITypeVariable<E>, TypeInstance<E>>();
	}

	public TypeInstance<E> get(ITypeVariable<E> variable)
	{
		if (variables.containsKey(variable))
			return variables.get(variable);
		else if (outer != null)
			return outer.get(variable);
		else
			throw new RuntimeException("Could not resolve type variable");
	}

	public void put(ITypeVariable<E> variable, TypeInstance<E> type)
	{
		variables.put(variable, type);
	}
}
