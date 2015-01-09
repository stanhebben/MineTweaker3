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
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.type.generic.TypeCapture;
import org.openzen.zencode.symbolic.unit.SymbolicExpansion;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface ITypeDefinition<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>>
{
	public List<ITypeVariable> getGenericParameters();
	
	public List<IMethod<E, T>> getConstructors(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public List<IMethod<E, T>> getInstanceMethods(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public List<IMethod<E, T>> getStaticMethods(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public IPartialExpression<E, T> getInstanceMember(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, String name, E instance);
	
	public IPartialExpression<E, T> getStaticMember(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, String name);
	
	public E getOperator(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, OperatorType operator, E... operands);
	
	public ICastingRule<E, T> getCastingRule(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, T toType);
	
	public void addExpansion(SymbolicExpansion<E, T> expansion);
	
	public T nullable(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public T nonNull(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public boolean isNullable();
	
	public E createDefaultValue(CodePosition position, IMethodScope<E, T> scope);
	
	public T getArrayBaseType(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public T getMapKeyType(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public T getMapValueType(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public List<T> predictOperatorArgumentType(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, OperatorType operator);
	
	public E compare(IMethodScope<E, T> scope, TypeCapture<E, T> typeCapture, CodePosition position, E left, E right, CompareType comparator);
	
	public MethodHeader<E, T> getFunctionHeader(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture);
	
	public List<T> getIteratorTypes(IModuleScope<E, T> scope, TypeCapture<E, T> typeCapture, int numArguments);
	
	public boolean isValidSwitchType();
	
	public boolean isStruct();
}
