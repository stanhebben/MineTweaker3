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
public interface IGenericType<E extends IPartialExpression<E>>
{
	public IGenericType<E> instance(TypeCapture<E> capture);
	
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, IGenericType<E> toType);
	
	public boolean canCastImplicit(IModuleScope<E> scope, IGenericType<E> toType);
	
	public boolean canCastExplicit(IModuleScope<E> scope, IGenericType<E> toType);
	
	public List<ICallable<E>> getVirtualCallers(IModuleScope<E> scope, E instance);
	
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope);
	
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope);
	
	public IGenericType<E> nullable();
	
	public IGenericType<E> nonNull();
	
	public boolean isNullable();
	
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, E instance, String name);
	
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, String name);
	
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope);
	
	public IGenericType<E> getArrayBaseType();
	
	public IGenericType<E> getMapKeyType();
	
	public IGenericType<E> getMapValueType();
	
	public List<IGenericType<E>> predictOperatorArgumentType(IModuleScope<E> scope, OperatorType operator);
	
	public E unary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E value);
	
	public E binary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E left, E right);
	
	public E ternary(CodePosition position, IMethodScope<E> scope, OperatorType operator, E first, E second, E third);
	
	public E compare(CodePosition position, IMethodScope<E> scope, E left, E right, CompareType comparator);
	
	public MethodHeader<E> getFunctionHeader();
	
	public List<IGenericType<E>> getForeachTypes(IMethodScope<E> scope, int numArguments);
	
	public boolean isValidSwitchType();
	
	public boolean isStruct();
	
	public boolean isInterface();
	
	public IGenericType<E> unify(IModuleScope<E> scope, IGenericType<E> other);
	
	public void addMember(IMember<E> member);
	
	public IGenericType<E> getSuperclass();
}
