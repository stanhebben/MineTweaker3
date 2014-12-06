/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java;

import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;

/**
 *
 * @author Stan
 */
public interface IJavaScopeGlobal extends IScopeGlobal<IJavaExpression, IJavaType>
{
	@Override
	public JavaTypeCompiler getTypes();
}
