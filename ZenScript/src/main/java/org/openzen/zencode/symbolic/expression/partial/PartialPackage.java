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
import org.openzen.zencode.symbolic.symbols.SymbolPackage;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.unit.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 * @param <E>
 */
public class PartialPackage<E extends IPartialExpression<E>> implements IPartialExpression<E>
{
	private final CodePosition position;
	private final IMethodScope<E> scope;
	private final SymbolPackage<E> contents;

	public PartialPackage(CodePosition position, IMethodScope<E> environment, SymbolPackage<E> contents)
	{
		this.position = position;
		this.scope = environment;
		this.contents = contents;
	}
	
	@Override
	public CodePosition getPosition()
	{
		return position;
	}
	
	@Override
	public IMethodScope<E> getScope()
	{
		return scope;
	}

	@Override
	public E eval()
	{
		scope.getErrorLogger().errorInvalidExpression(position, this);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E assign(CodePosition position, E other)
	{
		scope.getErrorLogger().errorCannotAssignTo(position, this);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public IPartialExpression<E> getMember(CodePosition position, String name)
	{
		IZenSymbol<E> member = contents.get(name);
		if (member == null) {
			scope.getErrorLogger().errorCouldNotResolveSymbol(position, name);
			return scope.getExpressionCompiler().invalid(position, scope);
		} else
			return member.instance(position, scope);
	}

	@Override
	public E call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		scope.getErrorLogger().errorCannotCall(position, this);
		return scope.getExpressionCompiler().invalid(position, scope);
	}
	
	@Override
	public E cast(CodePosition position, TypeInstance<E> type)
	{
		scope.getErrorLogger().errorInvalidExpression(position, this);
		return scope.getExpressionCompiler().invalid(position, scope, type);
	}

	@Override
	public IZenSymbol<E> toSymbol()
	{
		return null; // not supposed to be used as symbol
	}

	@Override
	public TypeInstance<E> getType()
	{
		return null;
	}

	@Override
	public TypeInstance<E> toType(List<TypeInstance<E>> types)
	{
		scope.getErrorLogger().errorNotAType(position, this);
		return scope.getTypeCompiler().getAny(scope);
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return Collections.emptyList();
	}

	@Override
	public IPartialExpression<E> via(SymbolicFunction<E> function)
	{
		return this;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}

	@Override
	public void validate()
	{
		
	}
}
