/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.Arrays;
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
 * @author Stan Hebben
 * @param <E>
 */
public class TypeInstance<E extends IPartialExpression<E>> implements IGenericType<E>
{
	private final ITypeDefinition<E> definition;
	private final TypeCapture<E> typeCapture;
	private final boolean nullable;
	
	public TypeInstance(ITypeDefinition<E> definition)
	{
		this(definition, Collections.emptyList(), false);
	}
	
	public TypeInstance(ITypeDefinition<E> definition, List<IGenericType<E>> typeArguments, boolean nullable)
	{
		if (definition.getGenericParameters().size() != typeArguments.size())
			throw new IllegalArgumentException("Type parameters don't match");
		
		this.definition = definition;
		this.nullable = nullable;
		typeCapture = new TypeCapture<>(null);
		for (int i = 0; i < typeArguments.size(); i++) {
			typeCapture.put(definition.getGenericParameters().get(i), typeArguments.get(i));
		}
	}
	
	public TypeInstance(ITypeDefinition<E> definition, TypeCapture<E> typeCapture, boolean nullable)
	{
		this.definition = definition;
		this.typeCapture = typeCapture;
		this.nullable = nullable;
	}
	
	public TypeCapture<E> getTypeCapture()
	{
		return typeCapture;
	}
	
	@Override
	public IGenericType<E> instance(TypeCapture<E> capture)
	{
		return new TypeInstance<>(definition, capture.instance(capture), nullable);
	}
	
	@Override
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, IGenericType<E> toType)
	{
		return definition.getCastingRule(scope, this, toType);
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
		return definition.getInstanceCallers(scope, instance, this);
	}
	
	@Override
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope)
	{
		return definition.getStaticCallers(scope, this);
	}
	
	@Override
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope)
	{
		return definition.getConstructors(scope, this);
	}
	
	@Override
	public TypeInstance<E> nullable()
	{
		if (nullable)
			return this;
		else
			return new TypeInstance<>(definition, typeCapture, true);
	}
	
	@Override
	public TypeInstance<E> nonNull()
	{
		if (nullable)
			return new TypeInstance<>(definition, typeCapture, false);
		else
			return this;
	}
	
	@Override
	public boolean isNullable()
	{
		return nullable;
	}
	
	@Override
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, E instance, String name)
	{
		return definition.getInstanceMember(position, scope, this, name, instance);
	}
	
	@Override
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, String name)
	{
		return definition.getStaticMember(position, scope, this, name);
	}
	
	@Override
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope)
	{
		return definition.createDefaultValue(position, scope, this);
	}
	
	@Override
	public IGenericType<E> getArrayBaseType()
	{
		return definition.getArrayBaseType(this);
	}
	
	@Override
	public IGenericType<E> getMapKeyType()
	{
		return definition.getMapKeyType(this);
	}
	
	@Override
	public IGenericType<E> getMapValueType()
	{
		return definition.getMapValueType(this);
	}
	
	@Override
	public List<IGenericType<E>> predictOperatorArgumentType(IModuleScope<E> scope, OperatorType operator)
	{
		return definition.predictOperatorArgumentType(this, operator);
	}
	
	@Override
	public E unary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E value)
	{
		return definition.getOperator(position, scope, this, operator, value, Collections.<E>emptyList());
	}
	
	@Override
	public E binary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E left, E right)
	{
		return definition.getOperator(position, scope, this, operator, left, Collections.singletonList(right));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E ternary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E first, E second, E third)
	{
		return definition.getOperator(position, scope, this, operator, first, Arrays.asList(second, third));
	}
	
	@Override
	public E compare(CodePosition position, IMethodScope<E> scope, E left, E right, CompareType comparator)
	{
		return definition.compare(position, scope, this, left, right, comparator);
	}
	
	@Override
	public MethodHeader<E> getFunctionHeader()
	{
		return definition.getFunctionHeader(this);
	}
	
	@Override
	public List<IGenericType<E>> getForeachTypes(IMethodScope<E> scope, int numArguments)
	{
		return definition.getForeachTypes(scope, this, numArguments);
	}
	
	@Override
	public boolean isValidSwitchType()
	{
		return definition.isValidSwitchType();
	}
	
	@Override
	public boolean isStruct()
	{
		return definition.isStruct();
	}

	@Override
	public boolean isInterface()
	{
		return definition.isInterface();
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
		definition.addMember(member);
	}
}
