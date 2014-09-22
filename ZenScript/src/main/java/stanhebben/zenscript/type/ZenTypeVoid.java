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
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeVoid extends ZenType {
	public ZenTypeVoid(IScopeGlobal environment) {
		super(environment);
	}
	
	@Override
	public IPartialExpression getMember(
			ZenPosition position,
			IScopeMethod environment,
			IPartialExpression value,
			String name) {
		environment.error(position, "void doesn't have members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getStaticMember(
			ZenPosition position,
			IScopeMethod environment,
			String name) {
		environment.error(position, "void doesn't have static members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IZenIterator makeIterator(int numValues) {
		return null;
	}

	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		
	}

	@Override
	public Type toASMType() {
		return Type.VOID_TYPE;
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return "V";
	}

	@Override
	public boolean isNullable() {
		return false;
	}
	
	@Override
	public Expression unary(
			ZenPosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public Expression binary(
			ZenPosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public Expression trinary(
			ZenPosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(
			ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}

	/*@Override
	public Expression call(
			ZenPosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		environment.error(position, "cannot call a void");
		return new ExpressionInvalid(position, this);
	}*/

	@Override
	public Class toJavaClass() {
		return void.class;
	}

	@Override
	public String getName() {
		return "void";
	}
	
	@Override
	public String getAnyClassName() {
		throw new UnsupportedOperationException("Cannot convert void to anything, not even any");
	}

	@Override
	public Expression defaultValue(ZenPosition position, IScopeMethod environment) {
		throw new RuntimeException("void has no default value");
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
