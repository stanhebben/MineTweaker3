/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.type;

import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArithmeticBinary;
import stanhebben.zenscript.expression.ExpressionArithmeticCompare;
import stanhebben.zenscript.expression.ExpressionArithmeticUnary;
import stanhebben.zenscript.expression.ExpressionInvalid;

/**
 *
 * @author Stan
 */
public abstract class ZenTypeArithmetic extends ZenTypePrimitive
{
	public ZenTypeArithmetic(IScopeGlobal scope)
	{
		super(scope);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}
	
	@Override
	public Expression operator(
			CodePosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values)
	{
		TypeRegistry types = getScope().getTypes();
		
		switch (operator) {
			case CAT:
				return types.STRING.operator(
						position,
						environment,
						OperatorType.CAT,
						values[0].cast(position, types.STRING),
						values[1].cast(position, types.STRING));
			case INVERT:
			case NEG:
				return new ExpressionArithmeticUnary(position, environment, operator, values[0]);
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case MOD:
			case AND:
			case OR:
			case XOR:
				return new ExpressionArithmeticBinary(position, environment, operator, values[0], values[1]);
			default:
				environment.error(position, getName() + " doesn't support this operation");
				return new ExpressionInvalid(position, environment);
		}
	}
	
	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type)
	{
		return new ExpressionArithmeticCompare(position, environment, type, left, right.cast(position, this));
	}

	@Override
	public boolean isNullable() {
		return false;
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
}
