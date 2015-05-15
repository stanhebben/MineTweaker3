/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedFunctionMember;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IMethodMember;
import org.openzen.zencode.symbolic.method.BoundCallable;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.IVirtualCallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class MethodMember<E extends IPartialExpression<E>>
	implements IMethodMember<E>
{
	private final ParsedFunctionMember source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	private final String name;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public MethodMember(IDefinitionScope<E> unit, int modifiers, String name, MethodHeader<E> header)
	{
		this.source = null;
		this.modifiers = modifiers;
		this.name = name;
		this.methodScope = new MethodScope<E>(unit, header, false);
	}
	
	public MethodMember(ParsedFunctionMember source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		this.name = source.getName();
		MethodHeader<E> header = source.getSignature().compile(unitScope);
		this.methodScope = new MethodScope<E>(unitScope, header, false);
	}
	
	public String getName()
	{
		return name;
	}
	
	public MethodHeader<E> getHeader()
	{
		return methodScope.getMethodHeader();
	}
	
	public ICallable<E> getStaticInstance(TypeInstance<E> instance)
	{
		return new StaticMethodCallable(methodScope.getMethodHeader().instance(instance));
	}
	
	public IVirtualCallable<E> getVirtualInstance(TypeInstance<E> instance)
	{
		return new VirtualMethodCallable(methodScope.getMethodHeader().instance(instance));
	}

	@Override
	public void completeContents()
	{
		if (source != null)
		{
			contents = source.getContents().compile(methodScope);
			annotations = SymbolicAnnotation.compileAll(source.getAnnotations(), methodScope);
		}
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
		return visitor.onMethod(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
	
	private class StaticMethodCallable implements ICallable<E>
	{
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		public StaticMethodCallable(InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.instancedMethodHeader = instancedMethodHeader;
		}

		@Override
		public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments)
		{
			return scope.getExpressionCompiler().callStaticMethod(position, scope, MethodMember.this, arguments);
		}

		@Override
		public E callWithConstants(CodePosition position, IMethodScope<E> scope, Object... values)
		{
			return call(position, scope, Expressions.convert(position, scope, values));
		}

		@Override
		public String getFullName()
		{
			return name;
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
	
	private class VirtualMethodCallable implements IVirtualCallable<E>
	{
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		public VirtualMethodCallable(InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.instancedMethodHeader = instancedMethodHeader;
		}

		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().callVirtualMethod(position, scope, MethodMember.this, instance, arguments);
		}

		@Override
		public String getFullName()
		{
			return name;
		}

		@Override
		public InstancedMethodHeader<E> getMethodHeader()
		{
			return instancedMethodHeader;
		}

		@Override
		public ICallable<E> bind(E instance)
		{
			return new BoundCallable<E>(this, instance);
		}
	}
}
