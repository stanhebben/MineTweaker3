/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import java.util.ArrayList;
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
public class BasicBinaryOperator<E extends IPartialExpression<E>> implements IOperatorMember<E>
{
	private final OperatorType operatorType;
	private final IBinaryOperator<E> operator;
	
	private final MethodHeader<E> methodHeader;
	
	public BasicBinaryOperator(
			IGenericType<E> typeInput1,
			IGenericType<E> typeInput2,
			IGenericType<E> typeOutput,
			OperatorType operatorType,
			IBinaryOperator<E> operator)
	{
		this.operatorType = operatorType;
		this.operator = operator;
		
		List<MethodParameter<E>> parameters = new ArrayList<MethodParameter<E>>();
		parameters.add(new MethodParameter<E>(CodePosition.SYSTEM, null, typeInput2, null));
		
		methodHeader = new MethodHeader<E>(
				CodePosition.SYSTEM,
				Collections.<GenericParameter<E>>emptyList(),
				typeOutput,
				parameters,
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
	
	public E operator(CodePosition position, IMethodScope<E> scope, E left, E right)
	{
		return operator.operator(position, scope, left, right);
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
			return operator(position, scope, instance, arguments.get(0));
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
