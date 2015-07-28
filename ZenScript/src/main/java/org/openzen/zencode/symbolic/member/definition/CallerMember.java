/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedCaller;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.member.ICallerMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CallerMember<E extends IPartialExpression<E>> implements ICallerMember<E>
{
	private final ParsedCaller source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public CallerMember(ParsedCaller source, IDefinitionScope<E> scope)
	{
		this.source = source;
		MethodHeader<E> methodHeader = source.getSignature().compile(scope);
		methodScope = new MethodScope<E>(scope, methodHeader, false);
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
	}
	
	@Override
	public ICallable<E> getStaticInstance(TypeInstance<E> instance)
	{
		return new StaticCallerCallable(methodScope.getMethodHeader().instance(instance));
	}
	
	@Override
	public IVirtualCallable<E> getVirtualInstance(TypeInstance<E> instance)
	{
		return new VirtualCallerCallable(methodScope.getMethodHeader().instance(instance));
	}

	@Override
	public void completeContents()
	{
		methodScope.getMethodHeader().completeContents(methodScope);
		contents = source.getContent().compile(methodScope);
		annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
	}

	@Override
	public void validate()
	{
		methodScope.getMethodHeader().validate(methodScope);
		contents.validate();
	}

	@Override
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public List<SymbolicAnnotation<E>> getAnnotations()
	{
		return annotations;
	}

	@Override
	public <R> R accept(IMemberVisitor<E, R> visitor)
	{
		return visitor.onCaller(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
	
	private class StaticCallerCallable implements ICallable<E>
	{
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		public StaticCallerCallable(InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.instancedMethodHeader = instancedMethodHeader;
		}

		@Override
		public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments)
		{
			return scope.getExpressionCompiler().callStaticCaller(position, scope, CallerMember.this, arguments);
		}

		@Override
		public E callWithConstants(CodePosition position, IMethodScope<E> scope, Object... values)
		{
			return call(
					position,
					scope,
					Expressions.convert(position, scope, values));
		}

		@Override
		public String getFullName()
		{
			return "()";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedMethodHeader;
		}

		@Override
		public E asValue(CodePosition position, IMethodScope<E> scope)
		{
			// TODO: implement
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
	
	private class VirtualCallerCallable implements IVirtualCallable<E>
	{
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		public VirtualCallerCallable(InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.instancedMethodHeader = instancedMethodHeader;
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().callVirtualCaller(position, scope, CallerMember.this, instance, arguments);
		}

		@Override
		public String getFullName()
		{
			return "()";
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedMethodHeader;
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<>(this, instance);
		}
	}
}
