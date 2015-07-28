/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyBool;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaAndAnd extends AbstractJavaExpression
{
	private final IJavaExpression left;
	private final IJavaExpression right;
	
	public JavaAndAnd(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		super(position, scope);
		
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void compileValue(MethodOutput method)
	{
		method.constant(0);

		Label lblFalse = new Label();
		left.compileElse(lblFalse, method);
		right.compileElse(lblFalse, method);
		method.pop();
		method.constant(1);
		method.label(lblFalse);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		Label exit = new Label();
		left.compileElse(exit, method);
		right.compileStatement(method);
		method.label(exit);
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput output)
	{
		Label exit = new Label();
		left.compileElse(exit, output);
		right.compileElse(exit, output);
		output.goTo(onIf);
		output.label(exit);
	}
	
	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		left.compileElse(onElse, output);
		right.compileElse(onElse, output);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().bool;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny leftValue = left.getCompileTimeValue();
		if (leftValue == null)
			return null;
		
		if (!leftValue.asBool())
			return AnyBool.FALSE;
		
		return right.getCompileTimeValue();
	}

	@Override
	public void validate()
	{
		left.validate();
		right.validate();
	}
}
