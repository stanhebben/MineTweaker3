/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
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
public class FunctionMember<E extends IPartialExpression<E>> implements ICallerMember<E>
{
	private final MethodHeader<E> methodHeader;
	
	public FunctionMember(MethodHeader<E> methodHeader)
	{
		this.methodHeader = methodHeader;
	}

	@Override
	public ICallable<E> getStaticInstance(TypeInstance<E> instance)
	{
		return null;
	}

	@Override
	public IVirtualCallable<E> getVirtualInstance(TypeInstance<E> instance)
	{
		return new VirtualInstance(instance);
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
		return visitor.onCaller(this);
	}
	
	private class VirtualInstance implements IVirtualCallable<E>
	{
		private final TypeInstance<E> instance;
		private final InstancedMethodHeader<E> methodHeader;
		
		public VirtualInstance(TypeInstance<E> instance)
		{
			this.instance = instance;
			methodHeader = FunctionMember.this.methodHeader.instance();
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().callFunction(position, scope, instance, arguments);
		}

		@Override
		public String getFullName()
		{
			return "call";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return methodHeader;
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
}
