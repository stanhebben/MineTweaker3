/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import static org.openzen.zencode.util.ZenTypeUtil.signature;

/**
 *
 * @author Stan
 */
public abstract class ZenTypeArithmeticNullable extends ZenType
{
	private final ZenType baseType;

	public ZenTypeArithmeticNullable(IScopeGlobal scope, ZenType baseType)
	{
		super(scope);

		this.baseType = baseType;
	}

	public ZenType getBaseType()
	{
		return baseType;
	}

	@Override
	public Expression operator(
			CodePosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values)
	{
		values[0] = values[0].cast(position, baseType);
		return baseType.operator(position, environment, operator, values);
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type)
	{
		return baseType.compare(position, environment, left.cast(position, baseType), right, type);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name)
	{
		return baseType.getMember(position, environment, value.eval().cast(position, baseType), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name)
	{
		return baseType.getStaticMember(position, environment, name);
	}

	@Override
	public IZenIterator makeIterator(int numValues)
	{
		return baseType.makeIterator(numValues);
	}

	@Override
	public boolean isNullable()
	{
		return true;
	}

	@Override
	public String getAnyClassName()
	{
		return getBaseType().getAnyClassName();
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment)
	{
		return new ExpressionNull(position, environment);
	}

	@Override
	public ZenType nullable()
	{
		return this;
	}

	@Override
	public ZenType nonNull()
	{
		return getBaseType();
	}

	@Override
	public Type toASMType()
	{
		return Type.getType(toJavaClass());
	}

	@Override
	public String getSignature()
	{
		return signature(toJavaClass());
	}
}
