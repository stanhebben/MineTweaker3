/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
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
 */
public class TypeInstance<E extends IPartialExpression<E>>
{
	private final IModuleScope<E> scope;
	private final ITypeDefinition<E> definition;
	private final TypeCapture<E> typeCapture;
	
	public TypeInstance(ITypeDefinition<E> definition, List<TypeInstance<E>> typeArguments, IModuleScope<E> scope)
	{
		if (definition.getGenericParameters().size() != typeArguments.size())
			throw new IllegalArgumentException("Type parameters don't match");
		
		this.scope = scope;
		this.definition = definition;
		typeCapture = new TypeCapture<E>(scope.getTypeCapture());
		for (int i = 0; i < typeArguments.size(); i++) {
			typeCapture.put(definition.getGenericParameters().get(i), typeArguments.get(i));
		}
	}
	
	private TypeInstance(ITypeDefinition<E> definition, TypeCapture<E> typeCapture, IModuleScope<E> scope)
	{
		this.scope = scope;
		this.definition = definition;
		this.typeCapture = typeCapture;
	}
	
	public IModuleScope<E> getScope()
	{
		return scope;
	}
	
	public ICastingRule<E> getCastingRule(TypeInstance<E> toType)
	{
		return definition.getCastingRule(scope, typeCapture, toType);
	}
	
	public boolean canCastImplicit(TypeInstance<E> toType)
	{
		ICastingRule<E> castingRule = getCastingRule(toType);
		return castingRule != null && !castingRule.isExplicit();
	}
	
	public boolean canCastExplicit(TypeInstance<E> toType)
	{
		return getCastingRule(toType) != null;
	}
	
	public List<IMethod<E>> getInstanceMethods()
	{
		return definition.getInstanceMethods(scope, typeCapture);
	}
	
	public List<IMethod<E>> getStaticMethods()
	{
		return definition.getStaticMethods(scope, typeCapture);
	}
	
	public List<IMethod<E>> getConstructors()
	{
		return definition.getConstructors(scope, typeCapture);
	}
	
	public TypeInstance<E> nullable()
	{
		return definition.nullable(scope, typeCapture);
	}
	
	public TypeInstance<E> nonNull()
	{
		return definition.nonNull(scope, typeCapture);
	}
	
	public boolean isNullable()
	{
		return definition.isNullable();
	}
	
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, E instance, String name)
	{
		return definition.getInstanceMember(scope, typeCapture, name, instance);
	}
	
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, String name)
	{
		return definition.getStaticMember(scope, typeCapture, name);
	}
	
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope)
	{
		return definition.createDefaultValue(position, scope);
	}
	
	public TypeInstance<E> getArrayBaseType()
	{
		return definition.getArrayBaseType(scope, typeCapture);
	}
	
	public TypeInstance<E> getMapKeyType()
	{
		return definition.getMapKeyType(scope, typeCapture);
	}
	
	public TypeInstance<E> getMapValueType()
	{
		return definition.getMapValueType(scope, typeCapture);
	}
	
	public List<TypeInstance<E>> predictOperatorArgumentType(OperatorType operator)
	{
		return definition.predictOperatorArgumentType(scope, typeCapture, operator);
	}
	
	@SuppressWarnings("unchecked")
	public E unary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E value)
	{
		return definition.getOperator(scope, typeCapture, operator, value);
	}
	
	@SuppressWarnings("unchecked")
	public E binary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E left, E right)
	{
		return definition.getOperator(scope, typeCapture, operator, left, right);
	}
	
	@SuppressWarnings("unchecked")
	public E ternary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E first, E second, E third)
	{
		return definition.getOperator(scope, typeCapture, operator, first, second, third);
	}
	
	public E compare(CodePosition position, IMethodScope<E> scope, E left, E right, CompareType comparator)
	{
		return definition.compare(scope, typeCapture, position, left, right, comparator);
	}
	
	public MethodHeader<E> getFunctionHeader()
	{
		return definition.getFunctionHeader(scope, typeCapture);
	}
	
	public List<TypeInstance<E>> getIteratorTypes(int numArguments)
	{
		return definition.getIteratorTypes(scope, typeCapture, numArguments);
	}
	
	public boolean isValidSwitchType()
	{
		return definition.isValidSwitchType();
	}
	
	public boolean isStruct()
	{
		return definition.isStruct();
	}
	
	public TypeInstance<E> instance(IModuleScope<E> scope, TypeCapture<E> capture)
	{
		// TODO: is this the correct way of doing it?
		return new TypeInstance<E>(definition, capture, scope);
	}
}
