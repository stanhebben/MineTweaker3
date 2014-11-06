/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.util.CodePosition;

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
	
	public abstract IPartialExpression getMemberLength(CodePosition position, IScopeMethod environment, IPartialExpression value);

	public abstract Expression indexGet(CodePosition position, IScopeMethod environment, Expression array, Expression index);
	
	public abstract Expression indexSet(CodePosition position, IScopeMethod environment, Expression array, Expression index, Expression value);
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public final int getNumberType() {
		return 0;
	}

	@Override
	public final IPartialExpression getMember(CodePosition position, IScopeMethod scope, IPartialExpression value, String name) {
		if (name.equals("length")) {
			return getMemberLength(position, scope, value);
		} else {
			MemberVirtual assembled = new MemberVirtual(position, scope, value.eval(), name);
			memberExpansion(assembled);
			
			if (assembled.isEmpty()) {
				scope.error(position, "no such member in array: " + name);
				return new ExpressionInvalid(position, scope);
			} else {
				return assembled;
			}
		}
	}

	@Override
	public final IPartialExpression getStaticMember(CodePosition position, IScopeMethod scope, String name) {
		MemberStatic assembled = new MemberStatic(position, scope, this, name);
		staticMemberExpansion(assembled);
		
		if (assembled.isEmpty()) {
			scope.error(position, "no such member in array: " + name);
			return new ExpressionInvalid(position, scope);
		} else {
			return assembled;
		}
	}

	@Override
	public final boolean isNullable() {
		return true;
	}
	
	@Override
	public final Expression operator(
			CodePosition position,
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
			CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		Expression compare = operator(position, environment, OperatorType.COMPARE, left, right);
		if (compare == null) {
			environment.error(position, "Arrays cannot be compared");
			return new ExpressionInvalid(position, environment);
		} else {
			return new ExpressionCompareGeneric(position, environment, compare, type);
		}
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
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
