/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.symbols;

import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ImportableSymbol<E extends IPartialExpression<E>> implements IZenSymbol<E>
{
	private final IImportable<E> importable;

	public ImportableSymbol(IImportable<E> importable)
	{
		this.importable = importable;
	}

	@Override
	public IPartialExpression<E> instance(CodePosition position, IMethodScope<E> scope)
	{
		return importable.toPartialExpression(position, scope);
	}

	@Override
	public IImportable<E> asImportable()
	{
		return importable;
	}
}
