/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.member;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.ISetterMember;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaExpansionSetterMember implements ISetterMember<IJavaExpression>
{
	private final String name;
	private final IJavaMethod method;
	
	public JavaExpansionSetterMember(String name, IJavaMethod method)
	{
		this.name = name;
		this.method = method;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IJavaExpression setStatic(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression value)
	{
		return new JavaCallStatic(position, scope, method, Collections.singletonList(value));
	}

	@Override
	public IJavaExpression setVirtual(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance, IJavaExpression value)
	{
		return new JavaCallStatic(position, scope, method, Arrays.asList(instance, value));
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
		return visitor.onSetter(this);
	}
}
