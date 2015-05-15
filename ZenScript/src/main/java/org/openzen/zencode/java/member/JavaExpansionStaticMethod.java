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
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IMethodMember;
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
public class JavaExpansionStaticMethod implements IMethodMember<IJavaExpression>
{
	private final String name;
	private final IJavaMethod method;
	
	public JavaExpansionStaticMethod(String name, IJavaMethod method)
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
		return new StaticCallable(method.getHeader().instance(instance));
	}

	@Override
	public IVirtualCallable<IJavaExpression> getVirtualInstance(TypeInstance<IJavaExpression> instance)
	{
		throw new RuntimeException("Not a virtual method");
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
	
	private class StaticCallable implements ICallable<IJavaExpression>
	{
		private final InstancedMethodHeader<IJavaExpression> instancedHeader;
		
		public StaticCallable(InstancedMethodHeader<IJavaExpression> instancedHeader)
		{
			this.instancedHeader = instancedHeader;
		}
		
		@Override
		public IJavaExpression call(CodePosition position, IMethodScope<IJavaExpression> scope, List<IJavaExpression> arguments)
		{
			return new JavaCallStatic(position, scope, method, arguments);
		}

		@Override
		public IJavaExpression callWithConstants(CodePosition position, IMethodScope<IJavaExpression> scope, Object... values)
		{
			return call(position, scope, Expressions.convert(position, scope, values));
		}

		@Override
		public String getFullName()
		{
			return method.getMethodName();
		}

		@Override
		public InstancedMethodHeader<IJavaExpression> getMethodHeader()
		{
			return instancedHeader;
		}

		@Override
		public IJavaExpression asValue(CodePosition position, IMethodScope<IJavaExpression> scope)
		{
			// TODO
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
}
