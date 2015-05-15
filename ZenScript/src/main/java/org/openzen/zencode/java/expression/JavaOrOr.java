/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.util.MethodOutput;
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
	public void compile(boolean pushResult, MethodOutput method)
	{
		if (pushResult) {
			method.constant(1);
			
			Label lblTrue = new Label();
			left.compileIf(lblTrue, method);
			right.compileIf(lblTrue, method);
			method.pop();
			method.constant(0);
			method.label(lblTrue);
		} else {
			Label exit = new Label();
			left.compileElse(exit, method);
			right.compile(false, method);
			method.label(exit);
		}
	}
	
	@Override
	public void compileIf(Label onIf, MethodOutput output)
	{
		
	}
	
	@Override
	public void compileElse(Label onElse, MethodOutput output)
	{
		
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public IAny getCompileTimeValue()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void validate()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
