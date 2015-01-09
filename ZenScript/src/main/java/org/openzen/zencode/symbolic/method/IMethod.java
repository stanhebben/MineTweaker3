/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E> expression type
 * @param <T>
 */
public interface IMethod<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	public E callStatic(CodePosition position, IMethodScope<E, T> scope, List<E> arguments);
	
	public E callStaticWithConstants(CodePosition position, IMethodScope<E, T> scope, Object... constantArguments);
	
	/**
	 * Returns null if the argument is null, calls the method otherwise.
	 * 
	 * @param position
	 * @param scope
	 * @param argument
	 * @return 
	 */
	public E callStaticNullable(CodePosition position, IMethodScope<E, T> scope, E argument);
	
	public E callVirtual(CodePosition position, IMethodScope<E, T> scope, E target, List<E> arguments);
	
	public E callVirtualWithConstants(CodePosition position, IMethodScope<E, T> scope, E target, Object... constantArguments);
	
	public boolean isStatic();
	
	public T getFunctionType();
	
	public MethodHeader<E, T> getMethodHeader();
	
	public T getReturnType();
	
	public void validateCall(CodePosition position, IMethodScope<E, T> scope, List<E> arguments);
}
