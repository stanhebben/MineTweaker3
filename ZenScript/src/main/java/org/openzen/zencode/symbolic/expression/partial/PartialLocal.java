/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class PartialLocal<E extends IPartialExpression<E>> extends AbstractPartialExpression<E>
{
	private final LocalSymbol<E> variable;

	public PartialLocal(CodePosition position, IMethodScope<E> scope, LocalSymbol<E> variable)
	{
		super(position, scope);
		
		this.variable = variable;
	}

	@Override
	public E eval()
	{
		return getScope().getExpressionCompiler().localGet(getPosition(), getScope(), variable);
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		return variable.getType().getInstanceMember(position, getScope(), eval(), name);
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		return getScope().getExpressionCompiler().localSet(position, getScope(), variable, other);
	}

	@Override
	public TypeInstance<E> getType()
	{
		return variable.getType();
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return variable.getType().getInstanceMethods();
	}
	
	@Override
	public E call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return function.addCapture(getPosition(), getScope(), variable);
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}
}
