/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Collections;
import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class CallableFunctionType<E extends IPartialExpression<E>> implements IGenericType<E>
{
	private final ICallable<E> method;
	
	public CallableFunctionType(ICallable<E> method)
	{
		this.method = method;
	}
	
	@Override
	public IGenericType<E> instance(TypeCapture<E> capture)
	{
		throw new UnsupportedOperationException("Cannot re-instance a function type");
	}

	@Override
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, IGenericType<E> toType)
	{
		// TODO: method casting rules
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean canCastImplicit(IModuleScope<E> scope, IGenericType<E> toType)
	{
		ICastingRule<E> castingRule = getCastingRule(scope, toType);
		return castingRule != null && !castingRule.isExplicit();
	}

	@Override
	public boolean canCastExplicit(IModuleScope<E> scope, IGenericType<E> toType)
	{
		return getCastingRule(scope, toType) != null;
	}

	@Override
	public List<ICallable<E>> getVirtualCallers(IModuleScope<E> scope, E instance)
	{
		return Collections.emptyList();
	}

	@Override
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope)
	{
		return Collections.singletonList(method);
	}

	@Override
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope)
	{
		return Collections.emptyList();
	}

	@Override
	public IGenericType<E> nullable()
	{
		return this;
	}

	@Override
	public IGenericType<E> nonNull()
	{
		return this;
	}

	@Override
	public boolean isNullable()
	{
		return false;
	}

	@Override
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, E instance, String name)
	{
		return null;
	}

	@Override
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, String name)
	{
		return null;
	}

	@Override
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope)
	{
		return null;
	}

	@Override
	public IGenericType<E> getArrayBaseType()
	{
		return null;
	}

	@Override
	public IGenericType<E> getMapKeyType()
	{
		return null;
	}

	@Override
	public IGenericType<E> getMapValueType()
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> predictOperatorArgumentType(IModuleScope<E> scope, OperatorType operator)
	{
		return null;
	}

	@Override
	public E unary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E value)
	{
		scope.getErrorLogger().errorNoSuchOperator(position, this, operator);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E binary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E left, E right)
	{
		scope.getErrorLogger().errorNoSuchOperator(position, this, operator);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E ternary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E first, E second, E third)
	{
		scope.getErrorLogger().errorNoSuchOperator(position, this, operator);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E compare(CodePosition position, IMethodScope<E> scope, E left, E right, CompareType comparator)
	{
		scope.getErrorLogger().errorNoSuchOperator(position, this, comparator.operator);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public MethodHeader<E> getFunctionHeader()
	{
		return method.getMethodHeader().getMethodHeader();
	}

	@Override
	public List<IGenericType<E>> getForeachTypes(IMethodScope<E> scope, int numArguments)
	{
		return null;
	}

	@Override
	public boolean isValidSwitchType()
	{
		return false;
	}

	@Override
	public boolean isStruct()
	{
		return false;
	}

	@Override
	public boolean isInterface()
	{
		return true;
	}

	@Override
	public IGenericType<E> unify(IModuleScope<E> scope, IGenericType<E> other)
	{
		ICastingRule<E> castingRuleA = this.getCastingRule(scope, other);
		ICastingRule<E> castingRuleB = other.getCastingRule(scope, this);
		
		if (castingRuleA != null && !castingRuleA.isExplicit())
			return other;
		else if (castingRuleB != null && !castingRuleB.isExplicit())
			return this;
		else
			return null;
	}

	@Override
	public void addMember(IMember<E> member)
	{
		throw new UnsupportedOperationException("Cannot add members to function types");
	}

	@Override
	public IGenericType<E> getSuperclass()
	{
		return null;
	}
}
