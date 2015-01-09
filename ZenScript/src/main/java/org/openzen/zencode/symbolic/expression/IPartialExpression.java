/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.expression;

import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan Hebben
 * 
 * @param <E> expression type
 * @param <T> type type
 */
public interface IPartialExpression<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public CodePosition getPosition();
	
	public IMethodScope<E, T> getScope();
	
	public E eval();
	
	public E assign(CodePosition position, E other);
	
	public IPartialExpression<E, T> getMember(CodePosition position, String name);
	
	public List<IMethod<E, T>> getMethods();
	
	/**
	 * Calls the given method from this expression. Method must be one of the
	 * methods offered by this expression, and arguments must match method parameters
	 * exactly.
	 * 
	 * @param position
	 * @param method
	 * @param arguments
	 * @return 
	 */
	public IPartialExpression<E, T> call(CodePosition position, IMethod<E, T> method, List<E> arguments);
	
	public E cast(CodePosition position, T type);
	
	public IZenSymbol<E, T> toSymbol();
	
	public T getType();
	
	public T toType(List<T> genericTypes);
	
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function);
	
	public IAny getCompileTimeValue();
	
	public void validate();
}
