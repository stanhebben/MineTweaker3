/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.expression.partial;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.symbols.SymbolType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public class PartialType<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
	extends AbstractPartialExpression<E, T>
{
	private final T type;
	
	public PartialType(CodePosition position, IScopeMethod<E, T> scope, T type) {
		super(position, scope);
		
		this.type = type;
	}

	@Override
	public E eval() {
		getScope().error(getPosition(), "cannot use type as expression");
		return getScope().getExpressionCompiler().invalid(getPosition(), getScope(), type);
	}

	@Override
	public E assign(CodePosition position, E value) {
		getScope().error(position, "cannot assign to a type");
		return getScope().getExpressionCompiler().invalid(position, getScope(), type);
	}

	@Override
	public IPartialExpression<E, T> getMember(CodePosition position, String name)
	{
		return type.getStaticMember(position, getScope(), name);
	}

	@Override
	public E call(CodePosition position, IMethod<E, T> method, E... arguments) {
		getScope().error(position, "cannot call a type");
		return getScope().getExpressionCompiler().invalid(position, getScope(), method.getReturnType());
	}

	@Override
	public IZenSymbol<E, T> toSymbol() {
		return new SymbolType<E, T>(type);
	}
	
	@Override
	public T getType() {
		return null; // not an expression
	}

	@Override
	public T toType(List<T> genericTypes) {
		return type;
	}

	@Override
	public List<IMethod<E, T>> getMethods() {
		return Collections.emptyList();
	}

	@Override
	public IPartialExpression<E, T> via(SymbolicFunction<E, T> function) {
		return this;
	}
}
