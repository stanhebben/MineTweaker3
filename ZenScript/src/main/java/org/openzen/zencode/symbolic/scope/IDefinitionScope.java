/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.symbolic.unit.ISymbolicDefinition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IDefinitionScope<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends IModuleScope<E, T>
{
	public ISymbolicDefinition<E, T> getDefinition();
}
