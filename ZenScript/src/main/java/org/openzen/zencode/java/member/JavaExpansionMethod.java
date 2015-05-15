/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IMethodMember;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaExpansionMethod implements IMethodMember<IJavaExpression>
{
	private final String name;
	private final IJavaMethod method;
	
	public JavaExpansionMethod(String name, IJavaMethod method)
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
	public ICallable<IJavaExpression> getStaticInstance(TypeInstance<IJavaExpression> instance)
	{
		return null;
	}

	@Override
	public IVirtualCallable<IJavaExpression> getVirtualInstance(TypeInstance<IJavaExpression> instance)
	{
		return new CallableMember(method.getHeader().withoutFirstParameter().instance(instance));
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
		// pass
	}

	@Override
	public void validate()
	{
		// pass
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<IJavaExpression> scope)
	{
		return true;
	}

	@Override
	public <R> R accept(IMemberVisitor<IJavaExpression, R> visitor)
	{
		return visitor.onMethod(this);
	}
	
	private class CallableMember implements IVirtualCallable<IJavaExpression>
	{
		private final InstancedMethodHeader<IJavaExpression> header;
		
		public CallableMember(InstancedMethodHeader<IJavaExpression> header)
		{
			this.header = header;
		}
		
		@Override
		public IJavaExpression call(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance, List<IJavaExpression> arguments)
		{
			List<IJavaExpression> joinedArguments = new ArrayList<>();
			joinedArguments.add(instance);
			joinedArguments.addAll(arguments);
			return new JavaCallStatic(position, scope, method, joinedArguments);
		}

		@Override
		public String getFullName()
		{
			return method.getMethodName();
		}

		@Override
		public InstancedMethodHeader<IJavaExpression> getMethodHeader()
		{
			return header;
		}

		@Override
		public ICallable<IJavaExpression> bind(IJavaExpression instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
}
