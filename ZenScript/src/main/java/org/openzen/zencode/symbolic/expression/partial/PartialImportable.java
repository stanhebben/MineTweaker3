/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.expression.partial;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class PartialImportable<E extends IPartialExpression<E>> implements IPartialExpression<E>
{
	private final CodePosition position;
	private final IMethodScope<E> scope;
	private final IImportable<E> importable;
	
	public PartialImportable(CodePosition position, IMethodScope<E> scope, IImportable<E> importable)
	{
		this.position = position;
		this.scope = scope;
		this.importable = importable;
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
		IImportable<E> subDefinition = importable.getSubDefinition(name);
		if (subDefinition != null)
			return new PartialImportable<E>(position, scope, subDefinition);
		
		IZenSymbol<E> member = importable.getMember(name);
		if (member != null)
			return member.instance(position, scope);
		
		scope.getErrorLogger().errorNoSuchMember(position, importable, name);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public List<IMethod<E>> getMethods()
	{
		return Collections.emptyList();
	}

	@Override
	public IPartialExpression<E> call(CodePosition position, IMethod<E> method, List<E> arguments)
	{
		scope.getErrorLogger().errorCannotCall(position, this);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E cast(CodePosition position, TypeInstance<E> type)
	{
		return eval().cast(position, type);
	}
	
	@Override
	public TypeInstance<E> getType()
	{
		return null;
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
