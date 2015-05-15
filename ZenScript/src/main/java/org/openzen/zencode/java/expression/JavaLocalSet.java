/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.LocalSymbol;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class JavaLocalSet extends AbstractJavaExpression
{
	private final LocalSymbol<IJavaExpression> variable;
	private final IJavaExpression value;
	
	public JavaLocalSet(
			CodePosition position,
			IMethodScope<IJavaExpression> environment,
			LocalSymbol<IJavaExpression> variable,
			IJavaExpression value)
	{
		super(position, environment);

		this.variable = variable;
		this.value = value;
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return variable.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		int local = output.getLocal(variable);

		value.compile(true, output);
		if (result)
			output.dup();
		output.store(variable.getType(), local);
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return value.getCompileTimeValue();
	}

	@Override
	public void validate()
	{
		if (!value.getType().canCastExplicit(getScope(), variable.getType()))
			getScope().getErrorLogger().errorCannotCastExplicit(getPosition(), value.getType(), variable.getType());
	}
}
