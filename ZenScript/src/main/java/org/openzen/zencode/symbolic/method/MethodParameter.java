/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class MethodParameter<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	private final String name;
	private final T type;
	private final E defaultValue;
	private SymbolLocal<E, T> local;

	public MethodParameter(String name, T type, E defaultValue)
	{
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public String getName()
	{
		return name;
	}

	public T getType()
	{
		return type;
	}

	public E getDefaultValue()
	{
		return defaultValue;
	}

	public SymbolLocal<E, T> getLocal()
	{
		if (local == null)
			local = new SymbolLocal<E, T>(type, false);

		return local;
	}
}
