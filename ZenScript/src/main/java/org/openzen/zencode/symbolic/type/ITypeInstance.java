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
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface ITypeInstance<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	public IModuleScope<E, T> getScope();
	
	public ICastingRule<E, T> getCastingRule(T toType);
	
	public boolean canCastImplicit(T toType);
	
	public boolean canCastExplicit(T toType);
	
	public List<IMethod<E, T>> getInstanceMethods();
	
	public List<IMethod<E, T>> getStaticMethods();
	
	public List<IMethod<E, T>> getConstructors();
	
	public T nullable();
	
	public T nonNull();
	
	public boolean isNullable();
	
	public IPartialExpression<E, T> getInstanceMember(CodePosition position, IMethodScope<E, T> scope, E instance, String name);
	
	public IPartialExpression<E, T> getStaticMember(CodePosition position, IMethodScope<E, T> scope, String name);
	
	public E createDefaultValue(CodePosition position, IMethodScope<E, T> scope);
	
	public T getArrayBaseType();
	
	public T getMapKeyType();
	
	public T getMapValueType();
	
	public List<T> predictOperatorArgumentType(OperatorType operator);
	
	public E unary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E value);
	
	public E binary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E left, E right);
	
	public E ternary(CodePosition position, IMethodScope<E, T> scope, OperatorType operator, E first, E second, E third);
	
	public E compare(CodePosition position, IMethodScope<E, T> scope, E left, E right, CompareType comparator);
	
	public MethodHeader<E, T> getFunctionHeader();
	
	public List<T> getIteratorTypes(int numArguments);
	
	public boolean isValidSwitchType();
	
	public boolean isStruct();
	
	public T instance(IModuleScope<E, T> scope);
	
	public T instance(IModuleScope<E, T> scope, Map<GenericParameter<E, T>, T> genericArguments);
}
