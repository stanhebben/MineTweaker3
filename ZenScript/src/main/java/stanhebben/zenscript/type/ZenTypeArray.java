/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.symbolic.MemberVirtual;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public abstract class ZenTypeArray extends ZenType {
	private final ZenType base;
	private final String name;
	
	public ZenTypeArray(ZenType base) {
		super(base.getScope());
		
		this.base = base;
		name = base + "[]";
	}
	
	public ZenType getBaseType() {
		return base;
	}
	
	public abstract IPartialExpression getMemberLength(ZenPosition position, IScopeMethod environment, IPartialExpression value);

	public abstract Expression indexGet(ZenPosition position, IScopeMethod environment, Expression array, Expression index);
	
	public abstract Expression indexSet(ZenPosition position, IScopeMethod environment, Expression array, Expression index, Expression value);
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final int getNumberType() {
		return 0;
	}

	@Override
	public final IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		if (name.equals("length")) {
			return getMemberLength(position, environment, value);
		} else {
			MemberVirtual assembled = new MemberVirtual(position, environment, this, name, false);
			

			IPartialExpression result = memberExpansion(position, environment, value.eval(), name);
			if (result == null) {
				environment.error(position, "no such member in array: " + name);
				return new ExpressionInvalid(position, environment);
			} else {
				return result;
			}
		}
	}

	@Override
	public final IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		IPartialExpression result = staticMemberExpansion(position, environment, name);
		if (result == null) {
			environment.error(position, "no such member in array: " + name);
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public final boolean isNullable() {
		return true;
	}
	
	@Override
	public final Expression operator(
			ZenPosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values) {
		Expression result = expandOperator(position, environment, operator, values);
		if (result == null) {
			environment.error(position, "Array has no such unary operator");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public final Expression compare(
			ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		Expression compare = operator(position, environment, OperatorType.COMPARE, left, right);
		if (compare == null) {
			environment.error(position, "Arrays cannot be compared");
			return new ExpressionInvalid(position, environment);
		} else {
			return new ExpressionCompareGeneric(position, environment, compare, type);
		}
	}

	@Override
	public Expression defaultValue(ZenPosition position, IScopeMethod environment) {
		return new ExpressionNull(position, environment);
	}
	
	@Override
	public ZenType nullable() {
		return this;
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
}
