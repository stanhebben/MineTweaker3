/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.java.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface IJavaExpression extends IPartialExpression<IJavaExpression, IJavaType>
{
	public void compile(boolean pushResult, MethodOutput method);
	
	public void compileIf(Label onIf, MethodOutput output);
	
	public void compileElse(Label onElse, MethodOutput output);
}
