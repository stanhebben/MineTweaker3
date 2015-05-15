/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser.generic;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.IGenericParameterBound;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;

/**
 *
 * @author Stan Hebben
 */
public interface IParsedGenericBound
{
	public <E extends IPartialExpression<E>> IGenericParameterBound<E> compile(IModuleScope<E> scope, ITypeVariable<E> typeVariable);
}
