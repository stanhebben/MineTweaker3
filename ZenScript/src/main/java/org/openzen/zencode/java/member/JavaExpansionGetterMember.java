/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.member;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.IGetterMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaExpansionGetterMember implements IGetterMember<IJavaExpression>
{
	private final String name;
	private final IJavaMethod method;
	
	public JavaExpansionGetterMember(String name, IJavaMethod method)
	{
		this.name = name;
		this.method = method;
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return method.getReturnType();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IJavaExpression getStatic(CodePosition position, IMethodScope<IJavaExpression> scope)
	{
		return new JavaCallStatic(position, scope, method, Collections.emptyList());
	}

	@Override
	public IJavaExpression getVirtual(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance)
	{
		return new JavaCallStatic(position, scope, method, Collections.singletonList(instance));
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
		return visitor.onGetter(this);
	}
}
