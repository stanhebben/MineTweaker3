/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.symbolic.type.casting.CastingRuleNone;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeNull extends ZenType {
	private static final Type TYPE = Type.getType(Object.class);
	
	public ZenTypeNull(IScopeGlobal environment) {
		super(environment);
	}
	
	@Override
	public ICastingRule getCastingRule(ZenType type) {
		if (type.isNullable()) {
			return new CastingRuleNone(this, type);
		} else {
			return null;
		}
	}

	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		
	}
	
	@Override
	public Expression unary(ZenPosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		environment.error(position, "null has no operators");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression binary(ZenPosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		environment.error(position, "null has no operators");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression trinary(ZenPosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		environment.error(position, "null has no operators");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		environment.error(position, "null has no operators");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		environment.error(position, "null doesn't have members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		environment.error(position, "null doesn't have static members");
		return new ExpressionInvalid(position, environment);
	}

	/*@Override
	public Expression call(
			ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
		environment.error(position, "cannot call null values");
		return new ExpressionInvalid(position);
	}*/

	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}

	@Override
	public boolean canCastExplicit(ZenType type) {
		return type.isNullable();
	}

	@Override
	public Class toJavaClass() {
		return Object.class;
	}

	@Override
	public Type toASMType() {
		return TYPE;
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return "Ljava/lang/Object;";
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return "null";
	}
	
	@Override
	public String getAnyClassName() {
		throw new UnsupportedOperationException("The null type does not have an any type");
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
		throw new UnsupportedOperationException("Null type cannot be non-null");
	}
}
