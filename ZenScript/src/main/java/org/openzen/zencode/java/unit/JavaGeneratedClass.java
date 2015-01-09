/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.unit;

import java.util.ArrayList;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.type.IJavaType;
import org.openzen.zencode.symbolic.scope.IGlobalScope;

/**
 *
 * @author Stan
 */
public class JavaGeneratedClass extends JavaUnit
{
	private final IGlobalScope<IJavaExpression, IJavaType> scope;
	private final String className;
	private final List<JavaGeneratedField> fields;
	private final List<JavaGeneratedMethod> methods;
	
	public JavaGeneratedClass(IGlobalScope<IJavaExpression, IJavaType> scope, String className)
	{
		this.scope = scope;
		this.className = className;
		fields = new ArrayList<JavaGeneratedField>();
		methods = new ArrayList<JavaGeneratedMethod>();
	}

	public IGlobalScope<IJavaExpression, IJavaType> getScope()
	{
		return scope;
	}
}
