/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.type.casting;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ICastingRule<E extends IPartialExpression<E>>
{
	public E cast(CodePosition position, IMethodScope<E> scope, E value);

	public TypeInstance<E> getInputType();

	public TypeInstance<E> getResultingType();
	
	public boolean isExplicit();
}
