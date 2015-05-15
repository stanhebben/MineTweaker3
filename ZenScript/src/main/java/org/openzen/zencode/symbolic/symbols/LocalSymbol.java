/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.expression.partial.PartialLocal;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class LocalSymbol<E extends IPartialExpression<E>> implements IZenSymbol<E>
{
	private final IGenericType<E> type;
	private final boolean isFinal;

	public LocalSymbol(IGenericType<E> type, boolean isFinal)
	{
		this.type = type;
		this.isFinal = isFinal;
	}

	public IGenericType<E> getType()
	{
		return type;
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> environment)
	{
		return new PartialLocal<E>(position, environment, this);
	}

	@Override
	public IImportable<E> asImportable()
	{
		return null;
	}
}
