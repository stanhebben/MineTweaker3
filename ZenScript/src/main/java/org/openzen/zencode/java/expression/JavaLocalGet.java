/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.symbols.SymbolLocal;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class JavaLocalGet extends AbstractJavaExpression
{
	private final SymbolLocal<IJavaExpression, IJavaType> variable;

	public JavaLocalGet(
			CodePosition position,
			IScopeMethod<IJavaExpression, IJavaType> environment,
			SymbolLocal<IJavaExpression, IJavaType> variable)
	{
		super(position, environment);

		this.variable = variable;
	}

	@Override
	public IJavaType getType()
	{
		return variable.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		int local = output.getLocal(variable);
		output.load(variable.getType().toASMType(), local);
	}
}
