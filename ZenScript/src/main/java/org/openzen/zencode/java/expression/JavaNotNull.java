/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaNotNull extends AbstractJavaExpression
{
	private final IJavaExpression value;
	
	public JavaNotNull(CodePosition position, IScopeMethod<IJavaExpression, IJavaType> scope, IJavaExpression value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public void compile(boolean pushResult, MethodOutput method)
	{
		value.compile(pushResult, method);
		
		if (pushResult) {
			Label labelElse = new Label();
			Label labelAfter = new Label();

			method.ifNull(labelElse);
			method.iConst1();
			method.goTo(labelAfter);
			method.label(labelElse);
			method.iConst0();
			method.label(labelAfter);
		}
	}

	@Override
	public IJavaType getType()
	{
		return getScope().getTypes().getBool();
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput output)
	{
		compile(true, output);
		output.ifNonNull(onIf);
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		compile(true, output);
		output.ifNull(onElse);
	}
}
