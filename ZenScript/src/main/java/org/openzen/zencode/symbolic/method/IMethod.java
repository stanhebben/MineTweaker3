/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E> expression type
 * @param <T>
 */
public interface IMethod<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public E callStatic(CodePosition position, IScopeMethod<E, T> scope, E... arguments);
	
	public E callStaticWithConstants(CodePosition position, IScopeMethod<E, T> scope, Object... constantArguments);
	
	/**
	 * Returns null if the argument is null, calls the method otherwise.
	 * 
	 * @param position
	 * @param scope
	 * @param argument
	 * @return 
	 */
	public E callStaticNullable(CodePosition position, IScopeMethod<E, T> scope, E argument);
	
	public E callVirtual(CodePosition position, IScopeMethod<E, T> scope, E target, E... arguments);
	
	public E callVirtualWithConstants(CodePosition position, IScopeMethod<E, T> scope, E target, Object... constantArguments);
	
	public boolean isStatic();
	
	public T getFunctionType();
	
	public MethodHeader<E, T> getMethodHeader();
	
	public T getReturnType();
}
