/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.IZenType;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IMethodScope<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends IDefinitionScope<E, T>
{
	public Statement<E, T> getControlStatement(String label);
	
	public MethodHeader<E, T> getMethodHeader();
	
	public T getReturnType();
}
