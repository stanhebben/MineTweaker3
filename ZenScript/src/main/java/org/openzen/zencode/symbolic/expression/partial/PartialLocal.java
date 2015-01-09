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
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 * @param <T>
 */
public class PartialLocal<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends AbstractPartialExpression<E, T>
{
	private final SymbolLocal<E, T> variable;

	public PartialLocal(CodePosition position, IMethodScope<E, T> scope, SymbolLocal<E, T> variable)
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
	public IPartialExpression<E, T> getMember(CodePosition position, String name)
	{
		return variable.getType().getInstanceMember(position, getScope(), eval(), name);
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		return getScope().getExpressionCompiler().localSet(position, getScope(), variable, other);
	}

	@Override
	public IZenSymbol<E, T> toSymbol()
	{
		return variable;
	}

	@Override
	public T getType()
	{
		return variable.getType();
	}

	@Override
	public T toType(List<T> genericTypes)
	{
		getScope().getErrorLogger().errorNotAType(getPosition(), this);
		return getScope().getTypeCompiler().getAny(getScope());
	}

	@Override
	public List<IMethod<E, T>> getMethods()
	{
		return variable.getType().getInstanceMethods();
	}
	
	@Override
	public E call(CodePosition position, IMethod<E, T> method, List<E> arguments)
	{
		return method.callVirtual(position, getScope(), eval(), arguments);
	}

	@Override
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function)
	{
		return function.addCapture(getPosition(), getScope(), variable);
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}
}
