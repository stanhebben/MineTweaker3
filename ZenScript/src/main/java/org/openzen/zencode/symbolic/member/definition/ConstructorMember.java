/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.parser.member.ParsedConstructor;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.expression.Expressions;
import org.openzen.zencode.symbolic.statement.Statement;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IConstructorMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.InstancedMethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class ConstructorMember<E extends IPartialExpression<E>> implements IConstructorMember<E>
{
	private final ParsedConstructor source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public ConstructorMember(IDefinitionScope<E> scope, int modifiers, MethodHeader<E> header, Statement<E> contents)
	{
		source = null;
		this.contents = contents;
		methodScope = new MethodScope<>(scope, header, true);
		this.modifiers = modifiers;
	}
	
	public ConstructorMember(ParsedConstructor source, IDefinitionScope<E> scope)
	{
		this.source = source;
		
		MethodHeader<E> definedHeader = source.getSignature().compile(scope);
		if (definedHeader.getReturnType() != null)
			scope.getErrorLogger().errorConstructorHasReturnType(definedHeader.getPosition());
		
		MethodHeader<E> actualHeader = new MethodHeader<>(
				definedHeader.getPosition(),
				definedHeader.getGenericParameters(),
				scope.getTypeCompiler().void_,
				definedHeader.getParameters(),
				definedHeader.isVarargs());
		
		methodScope = new MethodScope<>(scope, actualHeader, true);
		modifiers = Modifier.compileModifiers(source.getModifiers(), scope.getErrorLogger());
	}
	
	@Override
	public ICallable<E> instance(TypeInstance<E> instance)
	{
		return new ConstructorCallable(instance, methodScope.getMethodHeader().instance(instance));
	}
	
	// #################################
	// ### Implementation of IMember ###
	// #################################

	@Override
	public void completeContents()
	{
		methodScope.getMethodHeader().completeContents(methodScope);
		
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
		return visitor.onConstructor(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
	
	// #######################
	// ### Private Classes ###
	// #######################
	
	private class ConstructorCallable implements ICallable<E>
	{
		private final TypeInstance<E> type;
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		public ConstructorCallable(TypeInstance<E> type, InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.type = type;
			this.instancedMethodHeader = instancedMethodHeader;
		}
		
		@Override
		public E call(CodePosition position, IMethodScope<E> scope, List<E> arguments)
		{
			return scope.getExpressionCompiler().constructNew(position, scope, type, ConstructorMember.this, arguments);
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
			return instancedMethodHeader;
		}

		@Override
		public E asValue(CodePosition position, IMethodScope<E> scope)
		{
			// TODO: function expression
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
}
