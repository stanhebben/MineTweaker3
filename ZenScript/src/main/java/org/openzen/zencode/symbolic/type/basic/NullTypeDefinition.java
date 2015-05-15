/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.scope.IModuleScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.symbolic.type.TypeInstance;
import org.openzen.zencode.symbolic.type.casting.CastingRuleAsIs;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.generic.ITypeVariable;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class NullTypeDefinition<E extends IPartialExpression<E>> implements ITypeDefinition<E>
{

	@Override
	public List<? extends ITypeVariable<E>> getGenericParameters()
	{
		return Collections.emptyList();
	}

	@Override
	public List<ICallable<E>> getConstructors(IModuleScope<E> scope, TypeInstance<E> forType)
	{
		return Collections.emptyList();
	}

	@Override
	public List<ICallable<E>> getInstanceCallers(IModuleScope<E> scope, E instance, TypeInstance<E> forType)
	{
		return Collections.emptyList();
	}

	@Override
	public List<ICallable<E>> getStaticCallers(IModuleScope<E> scope, TypeInstance<E> forType)
	{
		return Collections.emptyList();
	}

	@Override
	public IPartialExpression<E> getInstanceMember(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, String name, E instance)
	{
		scope.getErrorLogger().errorNullHasNoMembers(position);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public IPartialExpression<E> getStaticMember(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, String name)
	{
		scope.getErrorLogger().errorNullHasNoMembers(position);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public E getOperator(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, OperatorType operator, E instance, List<E> operands)
	{
		scope.getErrorLogger().errorNullHasNoMembers(position);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public ICastingRule<E> getCastingRule(IModuleScope<E> scope, TypeInstance<E> fromType, IGenericType<E> toType)
	{
		if (toType.isNullable())
			return new CastingRuleAsIs<>(fromType, toType);
		else
			return null;
	}

	@Override
	public void addMember(IMember<E> member)
	{
		throw new UnsupportedOperationException("Cannot add members to null");
	}

	@Override
	public E createDefaultValue(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType)
	{
		return scope.getExpressionCompiler().constantNull(position, scope);
	}

	@Override
	public TypeInstance<E> getArrayBaseType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public TypeInstance<E> getMapKeyType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public TypeInstance<E> getMapValueType(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> predictOperatorArgumentType(TypeInstance<E> forType, OperatorType operator)
	{
		return null;
	}

	@Override
	public E compare(CodePosition position, IMethodScope<E> scope, TypeInstance<E> forType, E left, E right, CompareType comparator)
	{
		scope.getErrorLogger().errorNullHasNoMembers(position);
		return scope.getExpressionCompiler().invalid(position, scope);
	}

	@Override
	public MethodHeader<E> getFunctionHeader(TypeInstance<E> forType)
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> getForeachTypes(IModuleScope<E> scope, TypeInstance<E> forType, int numArguments)
	{
		return null;
	}

	@Override
	public List<IGenericType<E>> getStructMemberTypes(TypeInstance<E> forType)
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
		return false;
	}

	@Override
	public IImportable<E> getSubDefinition(String name)
	{
		return null;
	}

	@Override
	public Collection<String> getSubDefinitionNames()
	{
		return Collections.emptySet();
	}

	@Override
	public IZenSymbol<E> getMember(String name)
	{
		return null;
	}

	@Override
	public IGenericType<E> toType(IModuleScope<E> scope, List<IGenericType<E>> genericTypes)
	{
		return scope.getTypeCompiler().null_;
	}

	@Override
	public IPartialExpression<E> toPartialExpression(CodePosition position, IMethodScope<E> scope)
	{
		scope.getErrorLogger().errorNullHasNoMembers(position);
		return scope.getExpressionCompiler().invalid(position, scope);
	}
}
