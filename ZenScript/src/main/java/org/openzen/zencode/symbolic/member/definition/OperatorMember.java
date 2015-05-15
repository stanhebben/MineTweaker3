/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.member.definition;

import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.parser.member.ParsedOperator;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.annotations.SymbolicAnnotation;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IDefinitionScope;
import org.openzen.zencode.symbolic.scope.MethodScope;
import org.openzen.zencode.symbolic.definition.ISymbolicDefinition;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.member.IMemberVisitor;
import org.openzen.zencode.symbolic.member.IOperatorMember;
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
public class OperatorMember<E extends IPartialExpression<E>>
	implements IMember<E>, IOperatorMember<E>
{
	private final ParsedOperator source;
	private final IMethodScope<E> methodScope;
	private final int modifiers;
	private final OperatorType operator;
	
	private Statement<E> contents;
	private List<SymbolicAnnotation<E>> annotations;
	
	public OperatorMember(IDefinitionScope<E> unit, int modifiers, OperatorType operator, MethodHeader<E> header)
	{
		this.source = null;
		this.modifiers = modifiers;
		this.operator = operator;
		this.methodScope = new MethodScope<E>(unit, header, false);
	}
	
	public OperatorMember(ParsedOperator source, IDefinitionScope<E> unitScope)
	{
		this.source = source;
		this.modifiers = Modifier.compileModifiers(source.getModifiers(), unitScope.getErrorLogger());
		this.operator = source.getType();
		MethodHeader<E> header = source.getSignature().compile(unitScope);
		this.methodScope = new MethodScope<E>(unitScope, header, false);
	}

	@Override
	public OperatorType getOperator()
	{
		return operator;
	}
	
	@Override
	public MethodHeader<E> getHeader()
	{
		return methodScope.getMethodHeader();
	}
	
	@Override
	public IVirtualCallable<E> instance(TypeInstance<E> instance)
	{
		return new OperatorCallable(methodScope.getMethodHeader().instance(instance));
	}

	@Override
	public void completeContents()
	{
		if (source != null)
		{
			contents = source.getContent().compile(methodScope);
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
		return visitor.onOperator(this);
	}

	@Override
	public boolean isAccessibleFrom(IModuleScope<E> scope)
	{
		return methodScope.getAccessScope().isAccessibleFrom(scope.getAccessScope(), modifiers);
	}
	
	private class OperatorCallable implements IVirtualCallable<E>
	{
		private final InstancedMethodHeader<E> instancedMethodHeader;
		
		private OperatorCallable(InstancedMethodHeader<E> instancedMethodHeader)
		{
			this.instancedMethodHeader = instancedMethodHeader;
		}

		@Override
		public E call(CodePosition position, IMethodScope<E> scope, E instance, List<E> arguments)
		{
			return scope.getExpressionCompiler().callOperator(position, scope, OperatorMember.this, instance, arguments);
		}

		@Override
		public String getFullName()
		{
			return operator.getOperatorString();
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
