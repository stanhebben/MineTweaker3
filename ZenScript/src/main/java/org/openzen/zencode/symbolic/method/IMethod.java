/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.method;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E> expression type
 */
public interface IMethod<E extends IPartialExpression<E>>
{
	public E callStatic(CodePosition position, IMethodScope<E> scope, List<E> arguments);
	
	
	public E callStaticWithConstants(CodePosition position, IMethodScope<E> scope, Object... constantArguments);
	
	/**
	 * Returns null if the argument is null, calls the method otherwise.
	 * 
	 * @param position
	 * @param scope
	 * @param argument
	 * @return 
	 */
	public E callStaticNullable(CodePosition position, IMethodScope<E> scope, E argument);
	
	public E callVirtual(CodePosition position, IMethodScope<E> scope, E target, List<E> arguments);
	
	public E callVirtualWithConstants(CodePosition position, IMethodScope<E> scope, E target, Object... constantArguments);
	
	public boolean isStatic();
	
	public TypeInstance<E> getFunctionType();
	
	public MethodHeader<E> getMethodHeader();
	
	public IGenericType<E> getReturnType();
	
	public void validateCall(CodePosition position, IMethodScope<E> scope, List<E> arguments);
}
