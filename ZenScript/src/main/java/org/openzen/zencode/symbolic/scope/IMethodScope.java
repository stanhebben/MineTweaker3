/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.TypeInstance;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface IMethodScope<E extends IPartialExpression<E>> extends IDefinitionScope<E>
{
	public Statement<E> getControlStatement(String label);
	
	public MethodHeader<E> getMethodHeader();
	
	public TypeInstance<E> getReturnType();
}
