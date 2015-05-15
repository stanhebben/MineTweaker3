/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import java.util.List;
import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.definition.SymbolicFunction;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.statement.StatementExpression;
import org.openzen.zencode.symbolic.statement.StatementReturn;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public abstract class AbstractJavaExpression implements IJavaExpression
{
	private final CodePosition position;
	private final IMethodScope<IJavaExpression> scope;

	public AbstractJavaExpression(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		this.position = position;
		this.scope = scope;
	}

	@Override
	public CodePosition getPosition()
	{
		return position;
	}

	@Override
	public IMethodScope<IJavaExpression> getScope()
	{
		return scope;
	}

	@Override
	public IJavaExpression cast(CodePosition position, IGenericType<IJavaExpression> type)
	{
		if (getType().equals(type))
			return this;
		else {
			ICastingRule<IJavaExpression> castingRule = getType().getCastingRule(getScope(), type);
			if (castingRule == null) {
				getScope().getErrorLogger().errorCannotCastExplicit(position, getType(), type);
				return getScope().getExpressionCompiler().invalid(position, getScope(), type);
			} else
				return castingRule.cast(position, scope, this);
		}
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput output)
	{
		if (getType() == getScope().getTypeCompiler().bool) {
			compile(true, output);
			output.ifNE(onIf);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNonNull(onIf);
		} else
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		if (getType() == getScope().getTypeCompiler().bool) {
			compile(true, output);
			output.ifEQ(onElse);
		} else if (getType().isNullable()) {
			compile(true, output);
			output.ifNull(onElse);
		} else
			throw new RuntimeException("cannot compile non-pointer non-boolean value to if condition");
	}

	public Statement<IJavaExpression> asStatement()
	{
		return new StatementExpression<>(position, scope, this);
	}
	
	public Statement<IJavaExpression> asReturnStatement()
	{
		return new StatementReturn<>(position, scope, this);
	}

	// #########################################
	// ### IPartialExpression implementation ###
	// #########################################
	
	@Override
	public IJavaExpression eval()
	{
		return this;
	}

	@Override
	public IJavaExpression assign(CodePosition position, IJavaExpression other)
	{
		scope.getErrorLogger().errorCannotAssignTo(position, other);
		return scope.getExpressionCompiler().invalid(position, scope, getType());
	}

	@Override
	public IPartialExpression<IJavaExpression> getMember(CodePosition position, String name)
	{
		return getType().getInstanceMember(position, getScope(), this, name);
	}

	@Override
	public List<ICallable<IJavaExpression>> getMethods()
	{
		return getType().getVirtualCallers(getScope(), this);
	}

	@Override
	public IPartialExpression<IJavaExpression> via(SymbolicFunction<IJavaExpression> function)
	{
		return this;
	}
}
