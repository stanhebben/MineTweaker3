/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.symbolic.type.basic;

import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.TypeDefinition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class BuiltinTypeDefinition<E extends IPartialExpression<E>> extends TypeDefinition<E>
{
	public void addUnaryOperator(
			IGenericType<E> typeInput,
			IGenericType<E> typeOutput,
			OperatorType operatorType,
			IUnaryOperator<E> operator)
	{
		addMember(new BasicUnaryOperator<>(typeInput, typeOutput, operatorType, operator));
	}
	
	public void addBinaryOperator(
			IGenericType<E> typeInput1,
			IGenericType<E> typeInput2,
			IGenericType<E> typeOutput,
			OperatorType operatorType,
			IBinaryOperator<E> operator)
	{
		addMember(new BasicBinaryOperator<>(typeInput1, typeInput2, typeOutput, operatorType, operator));
	}
	
	public void addGetter(
			String name,
			IGenericType<E> type,
			IUnaryOperator<E> getter)
	{
		addMember(new BasicGetter<>(type, name, getter));
	}
	
	public void addComparators(IGenericType<E> boolType, IGenericType<E> type, ICompareOperator<E> operator)
	{
		for (CompareType compareType : CompareType.values()) {
			IBinaryOperator<E> binaryOp = (position, scope, left, right) -> operator.compare(position, scope, compareType, left, right);
			addMember(new BasicBinaryOperator<>(type, type, boolType, compareType.operator, binaryOp));
		}
	}
	
	public void addCaster(IGenericType<E> fromType, IGenericType<E> toType, IUnaryOperator<E> caster)
	{
		addMember(new BasicCaster<>(fromType, toType, caster));
	}
}
