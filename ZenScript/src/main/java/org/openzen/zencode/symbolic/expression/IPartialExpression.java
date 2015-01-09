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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
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
	
	public List<IMethod<E>> getMethods();
	
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
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments);
	
	public E cast(CodePosition position, TypeInstance<E> type);
	
	public IZenSymbol<E> toSymbol();
	
	public TypeInstance<E> getType();
	
	public TypeInstance<E> toType(List<TypeInstance<E>> genericTypes);
	
	public IPartialExpression<E> via(SymbolicFunction<E> function);
	
	public IAny getCompileTimeValue();
	
	public void validate();
}
