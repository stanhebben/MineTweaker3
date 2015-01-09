/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.List;
import java.util.Map;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.generic.GenericParameter;
import org.openzen.zencode.symbolic.method.IMethod;
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
 * @param <T>
 */
public class TypeInstance<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> implements ITypeInstance<E, T>
{
	private final IModuleScope<E, T> scope;
	private final ITypeDefinition<E, T> definition;
	private final TypeCapture<E, T> typeCapture;
	
	public TypeInstance(ITypeDefinition<E, T> definition, List<TypeInstance<E, T>> typeArguments, IModuleScope<E, T> scope)
	{
		if (definition.getGenericParameters().size() != typeArguments.size())
			throw new IllegalArgumentException("Type parameters don't match");
		
		this.scope = scope;
		this.definition = definition;
		typeCapture = new TypeCapture<E, T>(scope.getTypeCapture());
	}

	@Override
	public IModuleScope<E, T> getScope()
	{
		return scope;
	}

	@Override
	public ICastingRule<E, T> getCastingRule(T toType)
	{
		return definition.getCastingRule(scope, typeCapture, toType);
	}

	@Override
	public boolean canCastImplicit(T toType)
	{
		ICastingRule<E, T> castingRule = getCastingRule(toType);
		return castingRule != null && !castingRule.isExplicit();
	}

	@Override
	public boolean canCastExplicit(T toType)
	{
		return getCastingRule(toType) != null;
	}

	@Override
	public List<IMethod<E, T>> getInstanceMethods()
	{
		return definition.getInstanceMethods(scope, typeCapture);
	}

	@Override
	public List<IMethod<E, T>> getStaticMethods()
	{
		return definition.getStaticMethods(scope, typeCapture);
	}

	@Override
	public List<IMethod<E, T>> getConstructors()
	{
		return definition.getConstructors(scope, typeCapture);
	}

	@Override
	public T nullable()
	{
		return definition.nullable(scope, typeCapture);
	}

	@Override
	public T nonNull()
	{
		return definition.nonNull(scope, typeCapture);
	}

	@Override
	public boolean isNullable()
	{
		return definition.isNullable();
	}

	@Override
	public IPartialExpression<E, T> getInstanceMember(CodePosition position, IMethodScope<E, T> scope, E instance, String name)
	{
		return definition.getInstanceMember(scope, typeCapture, name, instance);
	}

	@Override
	public IPartialExpression<E, T> getStaticMember(CodePosition position, IMethodScope<E, T> scope, String name)
	{
		return definition.getStaticMember(scope, typeCapture, name);
	}

	@Override
	public E createDefaultValue(CodePosition position, IMethodScope<E, T> scope)
	{
		return definition.createDefaultValue(position, scope);
	}

	@Override
	public T getArrayBaseType()
	{
		return definition.getArrayBaseType(scope, typeCapture);
	}

	@Override
	public T getMapKeyType()
	{
		return definition.getMapKeyType(scope, typeCapture);
	}

	@Override
	public T getMapValueType()
	{
		return definition.getMapValueType(scope, typeCapture);
	}

	@Override
	public List<T> predictOperatorArgumentType(OperatorType operator)
	{
		return definition.predictOperatorArgumentType(scope, typeCapture, operator);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E unary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E value)
	{
		return definition.getOperator(scope, typeCapture, operator, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E binary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E left, E right)
	{
		return definition.getOperator(scope, typeCapture, operator, left, right);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E ternary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E first, E second, E third)
	{
		return definition.getOperator(scope, typeCapture, operator, first, second, third);
	}

	@Override
	public E compare(CodePosition position, IMethodScope<E, T> scope, E left, E right, CompareType comparator)
	{
		return definition.compare(scope, typeCapture, position, left, right, comparator);
	}

	@Override
	public MethodHeader<E, T> getFunctionHeader()
	{
		return definition.getFunctionHeader(scope, typeCapture);
	}

	@Override
	public List<T> getIteratorTypes(int numArguments)
	{
		return definition.getIteratorTypes(scope, typeCapture, numArguments);
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
	public T instance(IModuleScope<E, T> scope)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public T instance(IModuleScope<E, T> scope, Map<GenericParameter<E, T>, T> genericArguments)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
