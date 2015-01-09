/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.expression.partial;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialType<E extends IPartialExpression<E>> extends AbstractPartialExpression<E>
{
	private final TypeInstance<E> type;
	
	public PartialType(CodePosition position, IMethodScope<E> scope, TypeInstance<E> type)
	{
		super(position, scope);
		
		this.type = type;
	}

	@Override
	public E eval()
	{
		getScope().getErrorLogger().errorInvalidExpression(getPosition(), this);
		return getScope().getExpressionCompiler().invalid(getPosition(), getScope(), type);
	}

	@Override
	public E assign(CodePosition position, E value)
	{
		getScope().getErrorLogger().errorCannotAssignTo(position, this);
		return getScope().getExpressionCompiler().invalid(position, getScope(), type);
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		return type.getStaticMember(position, getScope(), name);
	}

	@Override
	public E call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		getScope().getErrorLogger().errorCannotCall(position, this);
		return getScope().getExpressionCompiler().invalid(position, getScope(), method.getReturnType());
	}

	@Override
	public IZenSymbol<E> toSymbol()
	{
		return new SymbolType<E>(type);
	}
	
	@Override
	public TypeInstance<E> getType() {
		return null; // not an expression
	}

	@Override
	public TypeInstance<E> toType(List<TypeInstance<E>> genericTypes) {
		return type;
	}

	@Override
	public List<IMethod<E>> getMethods() {
		return Collections.emptyList();
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function) {
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}
}
