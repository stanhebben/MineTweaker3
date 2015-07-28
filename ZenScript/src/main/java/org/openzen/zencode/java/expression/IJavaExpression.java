/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.java.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface IJavaExpression extends IPartialExpression<IJavaExpression>
{
	/**
	 * Compiles a statement. The stack after this statement must be in the same
	 * state as it was before.
	 * 
	 * @param method method output
	 */
	public void compileStatement(MethodOutput method);
	
	/**
	 * Compiles a value. The value must be pushed on the stack.
	 * 
	 * @param method method output
	 */
	public void compileValue(MethodOutput method);
	
	public void compileIf(Label onIf, MethodOutput output);
	
	public void compileElse(Label onElse, MethodOutput output);
}
