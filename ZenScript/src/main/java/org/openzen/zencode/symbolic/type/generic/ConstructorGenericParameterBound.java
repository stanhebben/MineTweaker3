/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.generic;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IConstructorMember;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ConstructorGenericParameterBound<E extends IPartialExpression<E>>
	implements IGenericParameterBound<E>
{
	private final MethodHeader<E> header;
	private final ITypeVariable<E> typeVariable;
	
	public ConstructorGenericParameterBound(ITypeVariable<E> typeVariable, MethodHeader<E> header)
	{
		this.header = header;
		this.typeVariable = typeVariable;
	}
	
	public MethodHeader<E> getHeader()
	{
		return header;
	}
	
	public ITypeVariable<E> getTypeVariable()
	{
		return typeVariable;
	}

	@Override
	public void completeContents(IMethodScope<E> scope)
	{
		header.completeContents(scope);
	}

	@Override
	public ICallable<E> getConstructor(TypeInstance<E> instance)
	{
		return new GenericConstructorMember().instance(instance);
	}

	@Override
	public IGenericType<E> getBound()
	{
		return null;
	}
	
	private class GenericConstructorMember implements IConstructorMember<E>
	{

		@Override
		public ICallable<E> instance(TypeInstance<E> instance)
		{
			return new ConstructorCallable(header.instance(), instance);
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
		public boolean isAccessibleFrom(IModuleScope<E> scope)
		{
			return true;
		}

		@Override
		public <R> R accept(IMemberVisitor<E, R> visitor)
		{
			return visitor.onConstructor(this);
		}
	}
	
	private class ConstructorCallable implements ICallable<E>
	{
		private final InstancedMethodHeader<E> instancedHeader;
		private final TypeInstance<E> typeInstance;
		
		private ConstructorCallable(InstancedMethodHeader<E> instancedHeader, TypeInstance<E> typeInstance)
		{
			this.instancedHeader = instancedHeader;
			this.typeInstance = typeInstance;
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments)
		{
			E source = scope.getThis(position, null);
			return scope.getExpressionCompiler().constructNewGenericOnObject(
					position,
					scope,
					source,
					ConstructorGenericParameterBound.this,
					arguments);
		}

		@Override
		public E callWithConstants(CodePosition position, IMethodScope<E> scope, Object... values)
		{
			return call(position, scope, Expressions.convert(position, scope, values));
		}

		@Override
		public String getFullName()
		{
			return "this";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedHeader;
		}

		@Override
		public E asValue(CodePosition position, IMethodScope<E> scope)
		{
			throw new UnsupportedOperationException("Cannot use a constructor as value");
		}
	}
}
