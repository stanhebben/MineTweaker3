/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionInvalid;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

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
			CodePosition position,
			IScopeMethod environment,
			IPartialExpression value,
			String name) {
		environment.error(position, "void doesn't have members");
		return new ExpressionInvalid(position, environment);
	}

	@Override
	public IPartialExpression getStaticMember(
			CodePosition position,
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
	public void constructCastingRules(AccessScope access, ICastingRuleDelegate rules, boolean followCasters) {
		
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
	public Expression operator(
			CodePosition position, IScopeMethod environment, OperatorType operator, Expression... values)
	{
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}
	
	@Override
	public Expression compare(
			CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type)
	{
		environment.error(position, "void does not have operators");
		return new ExpressionInvalid(position, environment);
	}

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
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
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
