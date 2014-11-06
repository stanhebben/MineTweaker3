/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.method.JavaMethod;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import static org.openzen.zencode.runtime.IAny.NUM_INT;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableStaticMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableVirtualMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleVirtualMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ZenTypeIntObject extends ZenType {
	private final ZenType INT;
	
	public ZenTypeIntObject(IScopeGlobal environment, ZenType INT) {
		super(environment);
		
		this.INT = INT;
	}

	@Override
	public Expression unary(CodePosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		return INT.unary(position, environment, value.cast(position, INT), operator);
	}

	@Override
	public Expression binary(CodePosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		return INT.binary(position, environment, left.cast(position, INT), right, operator);
	}

	@Override
	public Expression trinary(CodePosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		return INT.trinary(position, environment, first.cast(position, INT), second, third, operator);
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return INT.compare(position, environment, left.cast(position, INT), right, type);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return INT.getMember(position, environment, value.eval().cast(position, INT), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		return INT.getStaticMember(position, environment, name);
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		return INT.call(position, environment, receiver.cast(position, INT), arguments);
	}*/

	@Override
	public IZenIterator makeIterator(int numValues) {
		return INT.makeIterator(numValues);
	}

	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.BYTE, new CastingRuleVirtualMethod(this, methods.BYTE_VALUE));
		rules.registerCastingRule(types.BYTEOBJECT, new CastingRuleNullableStaticMethod(
				methods.BYTE_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.BYTE_VALUE)));
		rules.registerCastingRule(types.SHORT, new CastingRuleVirtualMethod(this, methods.SHORT_VALUE));
		rules.registerCastingRule(types.SHORTOBJECT, new CastingRuleNullableStaticMethod(
				methods.SHORT_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.SHORT_VALUE)));
		rules.registerCastingRule(types.INT, new CastingRuleVirtualMethod(this, methods.INT_VALUE));
		rules.registerCastingRule(types.INTOBJECT, new CastingRuleNullableStaticMethod(
				methods.INT_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.INT_VALUE)));
		rules.registerCastingRule(types.LONG, new CastingRuleVirtualMethod(this, methods.LONG_VALUE));
		rules.registerCastingRule(types.LONGOBJECT, new CastingRuleNullableStaticMethod(
				methods.LONG_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.LONG_VALUE)));
		rules.registerCastingRule(types.FLOAT, new CastingRuleVirtualMethod(this, methods.FLOAT_VALUE));
		rules.registerCastingRule(types.FLOATOBJECT, new CastingRuleNullableStaticMethod(
				methods.FLOAT_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.FLOAT_VALUE)));
		rules.registerCastingRule(types.DOUBLE, new CastingRuleVirtualMethod(this, methods.DOUBLE_VALUE));
		rules.registerCastingRule(types.DOUBLEOBJECT, new CastingRuleNullableStaticMethod(
				methods.DOUBLE_VALUEOF,
				new CastingRuleVirtualMethod(this, methods.DOUBLE_VALUE)));
		
		rules.registerCastingRule(types.STRING, new CastingRuleNullableVirtualMethod(types.INTOBJECT, methods.INT_TOSTRING));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(
				JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, types.INT),
				new CastingRuleVirtualMethod(this, methods.DOUBLE_VALUE)));
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}

	@Override
	public Class toJavaClass() {
		return Integer.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(Integer.class);
	}

	@Override
	public int getNumberType() {
		return NUM_INT;
	}

	@Override
	public String getSignature() {
		return signature(Integer.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return "int?";
	}
	
	@Override
	public String getAnyClassName() {
		return INT.getAnyClassName();
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
		return INT;
	}
}
