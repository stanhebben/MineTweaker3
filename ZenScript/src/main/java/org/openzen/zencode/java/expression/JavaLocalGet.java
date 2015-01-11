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
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class JavaLocalGet extends AbstractJavaExpression
{
	private final LocalSymbol<IJavaExpression> variable;

	public JavaLocalGet(
			CodePosition position,
			IMethodScope<IJavaExpression> environment,
			LocalSymbol<IJavaExpression> variable)
	{
		super(position, environment);

		this.variable = variable;
	}

	@Override
	public TypeInstance<IJavaExpression> getType()
	{
		return variable.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		int local = output.getLocal(variable);
		output.load(variable.getType(), local);
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
