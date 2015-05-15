/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.member;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.ICasterMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IModuleScope;

/**
 *
 * @author Stan
 */
public class JavaNativeCaster implements ICasterMember<IJavaExpression>
{
	private final IJavaMethod method;
	
	public JavaNativeCaster(IJavaMethod method)
	{
		this.method = method;
	}

	@Override
	public int getModifiers()
	{
		return method.getZCModifiers();
	}

	@Override
	public List<SymbolicAnnotation<IJavaExpression>> getAnnotations()
	{
		return Collections.emptyList();
	}

	@Override
	public void completeContents()
	{
		// nothing to do
	}

	@Override
	public void validate()
	{
		// nothing to do
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<IJavaExpression> scope)
	{
		return true;
	}

	@Override
	public <R> R accept(IMemberVisitor<IJavaExpression, R> visitor)
	{
		return visitor.onCaster(this);
	}
}
