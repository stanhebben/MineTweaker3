/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.expression;

import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 * 
 * @param <E> expression type
 */
public interface IPartialExpression<E extends IPartialExpression<E>>
{
	public CodePosition getPosition();
	
	public IMethodScope<E> getScope();
	
	public E eval();
	
	public E assign(CodePosition position, E other);
	
	public IPartialExpression<E> getMember(CodePosition position, String name);
	
	public List<ICallable<E>> getMethods();
	
	public E cast(CodePosition position, IGenericType<E> type);
	
	public IGenericType<E> getType();
	
	public IPartialExpression<E> via(SymbolicFunction<E> function);
	
	public IAny getCompileTimeValue();
	
	public void validate();
}
