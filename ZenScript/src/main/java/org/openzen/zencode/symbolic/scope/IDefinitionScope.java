/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IDefinitionScope<E extends IPartialExpression<E>> extends IModuleScope<E>
{
	public ISymbolicDefinition<E> getDefinition();
	
	public IGenericType<E> getSelfType();
}
