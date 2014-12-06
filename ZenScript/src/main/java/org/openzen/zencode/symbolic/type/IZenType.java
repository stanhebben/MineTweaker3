/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 * @param <T>
 */
public interface IZenType<E extends IPartialExpression<E, T>, T extends IZenType<E, T>>
{
	public IScopeGlobal<E, T> getScope();
	
	public ICastingRule<E, T> getCastingRule(AccessScope access, T toType);
	
	public boolean canCastImplicit(AccessScope access, T toType);
	
	public boolean canCastExplicit(AccessScope access, T toType);
	
	public List<IMethod<E, T>> getInstanceMethods();
	
	public List<IMethod<E, T>> getStaticMethods();
	
	public List<IMethod<E, T>> getConstructors();
	
	public T nullable();
	
	public T nonNull();
	
	public boolean isNullable();
	
	public E call(CodePosition position, IScopeMethod<E, T> scope, IMethod<E, T> method, E... arguments);
	
	public IPartialExpression<E, T> getInstanceMember(CodePosition position, IScopeMethod<E, T> scope, E instance, String name);
	
	public IPartialExpression<E, T> getStaticMember(CodePosition position, IScopeMethod<E, T> scope, String name);
	
	public E createDefaultValue(CodePosition position, IScopeMethod<E, T> scope);
	
	public T getArrayBaseType();
	
	public T getMapKeyType();
	
	public T getMapValueType();
	
	public List<T> predictOperatorArgumentType(OperatorType operator);
	
	public E unary(CodePosition position, IScopeMethod<E, T> scope, OperatorType operator, E value);
	
	public E binary(CodePosition position, IScopeMethod<E, T> scope, OperatorType operator, E left, E right);
	
	public E ternary(CodePosition position, IScopeMethod<E, T> scope, OperatorType operator, E first, E second, E third);
	
	public E compare(CodePosition position, IScopeMethod<E, T> scope, E left, E right, CompareType comparator);
	
	public MethodHeader<E, T> getFunctionHeader();
	
	public List<T> getIteratorTypes(int numArguments);
}
