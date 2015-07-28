/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.AnyNull;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaOrOr extends AbstractJavaExpression
{
	private final IJavaExpression left;
	private final IJavaExpression right;
	
	public JavaOrOr(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression left, IJavaExpression right)
	{
		super(position, scope);
		
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void compileValue(MethodOutput method)
	{
		method.constant(1);

		Label lblTrue = new Label();
		left.compileIf(lblTrue, method);
		right.compileIf(lblTrue, method);
		method.pop();
		method.constant(0);
		method.label(lblTrue);
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
		left.compileIf(onIf, output);
		right.compileIf(onIf, output);
	}
	
	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		Label lblAfter = new Label();
		left.compileElse(lblAfter, output);
		right.compileElse(lblAfter, output);
		output.goTo(onElse);
		output.label(lblAfter);
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return left.getType().unify(getScope(), right.getType());
	}

	@Override
	public IAny getCompileTimeValue()
	{
		IAny leftValue = left.getCompileTimeValue();
		IAny rightValue = right.getCompileTimeValue();
		
		if (leftValue == null)
			return null;
		
		if (leftValue == AnyNull.INSTANCE)
			return rightValue;
		
		if (leftValue.canCastImplicit(boolean.class)) {
			if (leftValue.asBool())
				return rightValue;
			else
				return leftValue;
		}
		
		return null;
	}

	@Override
	public void validate()
	{
		
	}
}
