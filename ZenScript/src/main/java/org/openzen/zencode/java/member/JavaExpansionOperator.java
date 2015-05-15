/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.member;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.java.expression.IJavaExpression;
import org.openzen.zencode.java.expression.JavaCallStatic;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IOperatorMember;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaExpansionOperator implements IOperatorMember<IJavaExpression>
{
	private final OperatorType operator;
	private final IJavaMethod method;
	private final MethodHeader<IJavaExpression> methodHeader;
	
	public JavaExpansionOperator(OperatorType operator, IJavaMethod method)
	{
		this.operator = operator;
		this.method = method;
		methodHeader = method.getHeader().withoutFirstParameter();
	}
	
	@Override
	public MethodHeader<IJavaExpression> getHeader()
	{
		return method.getHeader();
	}

	@Override
	public OperatorType getOperator()
	{
		return operator;
	}

	@Override
	public IVirtualCallable<IJavaExpression> instance(TypeInstance<IJavaExpression> instance)
	{
		return new OperatorCallable(methodHeader.instance(instance));
	}

	@Override
	public int getModifiers()
	{
		return method.getZCModifiers() & ~Modifier.STATIC.getCode();
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
		return visitor.onOperator(this);
	}
	
	private class OperatorCallable implements IVirtualCallable<IJavaExpression>
	{
		private final InstancedMethodHeader<IJavaExpression> methodHeader;
		
		public OperatorCallable(InstancedMethodHeader<IJavaExpression> methodHeader)
		{
			this.methodHeader = methodHeader;
		}
		
		@Override
		public IJavaExpression call(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaExpression instance, List<IJavaExpression> arguments)
		{
			return new JavaCallStatic(position, scope, method, arguments);
		}

		@Override
		public String getFullName()
		{
			return operator.getOperatorString();
		}

		@Override
		public InstancedMethodHeader<IJavaExpression> getMethodHeader()
		{
			return methodHeader;
		}

		@Override
		public ICallable<IJavaExpression> bind(IJavaExpression instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
}
