/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type;

import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public interface ITypeDefinition<E extends IPartialExpression<E>> extends IImportable<E>
{
	public List<? extends ITypeVariable<E>> getGenericParameters();
	
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope, TypeInstance<E> forType);
	
	public List<ICallable<E>> getInstanceCallers(IModuleScope<E> scope, E instance, TypeInstance<E> forType);
	
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope, TypeInstance<E> forType);
	
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, String name, E instance);
	
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, String name);
	
	public E getOperator(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, OperatorType operator, E instance, List<E> operands);
	
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, TypeInstance<E> fromType, IGenericType<E> toType);
	
	public void addMember(IMember<E> member);
	
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType);
	
	public IGenericType<E> getArrayBaseType(TypeInstance<E> forType);
	
	public IGenericType<E> getMapKeyType(TypeInstance<E> forType);
	
	public IGenericType<E> getMapValueType(TypeInstance<E> forType);
	
	public List<IGenericType<E>> predictOperatorArgumentType(TypeInstance<E> forType, OperatorType operator);
	
	public E compare(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, E left, E right, CompareType comparator);
	
	public MethodHeader<E> getFunctionHeader(TypeInstance<E> forType);
	
	public List<IGenericType<E>> getForeachTypes(IModuleScope<E> scope, TypeInstance<E> forType, int numArguments);
	
	public List<IGenericType<E>> getStructMemberTypes(TypeInstance<E> forType);
	
	public boolean isValidSwitchType();
	
	public boolean isStruct();
	
	public boolean isInterface();
	
	public IGenericType<E> getSuperType();
}
