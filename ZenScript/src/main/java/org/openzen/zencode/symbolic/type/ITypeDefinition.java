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
 */
public interface ITypeDefinition<E extends IPartialExpression<E>>
{
	public List<ITypeVariable<E>> getGenericParameters();
	
	public List<IMethod<E>> getConstructors(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public List<IMethod<E>> getInstanceMethods(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public List<IMethod<E>> getStaticMethods(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public IPartialExpression<E> getInstanceMember(IModuleScope<E> scope, TypeCapture<E> typeCapture, String name, E instance);
	
	public IPartialExpression<E> getStaticMember(IModuleScope<E> scope, TypeCapture<E> typeCapture, String name);
	
	public E getOperator(IModuleScope<E> scope, TypeCapture<E> typeCapture, OperatorType operator, E... operands);
	
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, TypeCapture<E> typeCapture, TypeInstance<E> toType);
	
	public void addExpansion(SymbolicExpansion<E> expansion);
	
	public TypeInstance<E> nullable(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public TypeInstance<E> nonNull(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public boolean isNullable();
	
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope);
	
	public TypeInstance<E> getArrayBaseType(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public TypeInstance<E> getMapKeyType(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public TypeInstance<E> getMapValueType(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public List<TypeInstance<E>> predictOperatorArgumentType(IModuleScope<E> scope, TypeCapture<E> typeCapture, OperatorType operator);
	
	public E compare(IMethodScope<E> scope, TypeCapture<E> typeCapture, CodePosition position, E left, E right, CompareType comparator);
	
	public MethodHeader<E> getFunctionHeader(IModuleScope<E> scope, TypeCapture<E> typeCapture);
	
	public List<TypeInstance<E>> getIteratorTypes(IModuleScope<E> scope, TypeCapture<E> typeCapture, int numArguments);
	
	public boolean isValidSwitchType();
	
	public boolean isStruct();
}
