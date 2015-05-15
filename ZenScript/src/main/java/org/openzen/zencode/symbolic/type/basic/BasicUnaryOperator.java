/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IOperatorMember;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class BasicUnaryOperator<E extends IPartialExpression<E>> implements IOperatorMember<E>
{
	private final OperatorType operatorType;
	
	private final MethodHeader<E> methodHeader;
	private final IUnaryOperator<E> operator;
	
	public BasicUnaryOperator(
			IGenericType<E> typeInput,
			IGenericType<E> typeOutput,
			OperatorType operatorType,
			IUnaryOperator<E> operator)
	{
		this.operatorType = operatorType;
		this.operator = operator;
		
		methodHeader = new MethodHeader<E>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<E>>emptyList(),
				typeOutput,
				Collections.<MethodParameter<E>>emptyList(),
				false);
	}

	@Override
	public MethodHeader<E> getHeader()
	{
		return methodHeader;
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return true;
	}

	@Override
	public OperatorType getOperator()
	{
		return operatorType;
	}

	@Override
	public IVirtualCallable<E> instance(TypeInstance<E> instance)
	{
		return new OperatorCallable(instance);
	}

	@Override
	public int getModifiers()
	{
		return Modifier.EXPORT.getCode();
	}

	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return Collections.emptyList();
	}

	@Override
	public void completeContents()
	{
		
	}

	@Override
	public void validate()
	{
		
	}

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onOperator(this);
	}
	
	public E operator(CodePosition position, IMethodScope<E> scope, E value)
	{
		return operator.operator(position, scope, value);
	}
	
	private class OperatorCallable implements IVirtualCallable<E>
	{
		private final TypeInstance<E> instance;
		
		public OperatorCallable(TypeInstance<E> instance)
		{
			this.instance = instance;
		}

		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return operator(position, scope, instance);
		}

		@Override
		public String getFullName()
		{
			return operatorType.getOperatorString();
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return methodHeader.instance(instance);
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<E>(this, instance);
		}
	}
}
