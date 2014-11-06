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
import static org.openzen.zencode.runtime.IAny.NUM_FLOAT;
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
public class ZenTypeFloatObject extends ZenType {
	private final ZenType FLOAT;
	
	public ZenTypeFloatObject(IScopeGlobal environment, ZenType FLOAT) {
		super(environment);
		
		this.FLOAT = FLOAT;
	}

	@Override
	public Expression unary(CodePosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		return FLOAT.unary(position, environment, value.cast(position, FLOAT), operator);
	}

	@Override
	public Expression binary(CodePosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		return FLOAT.binary(position, environment, left.cast(position, FLOAT), right, operator);
	}

	@Override
	public Expression trinary(CodePosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		return FLOAT.trinary(position, environment, first.cast(position, FLOAT), second, third, operator);
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return FLOAT.compare(position, environment, left.cast(position, FLOAT), right, type);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return FLOAT.getMember(position, environment, value.eval().cast(position, FLOAT), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		return FLOAT.getStaticMember(position, environment, name);
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		return FLOAT.call(position, environment, receiver.cast(position, FLOAT), arguments);
	}*/

	@Override
	public IZenIterator makeIterator(int numValues) {
		return FLOAT.makeIterator(numValues);
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
		
		rules.registerCastingRule(types.STRING, new CastingRuleNullableVirtualMethod(types.FLOATOBJECT, methods.FLOAT_TOSTRING));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(
				JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, FLOAT),
				new CastingRuleVirtualMethod(this, methods.FLOAT_VALUE)));
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}

	@Override
	public Class toJavaClass() {
		return Float.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(Float.class);
	}

	@Override
	public int getNumberType() {
		return NUM_FLOAT;
	}

	@Override
	public String getSignature() {
		return signature(Float.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return "float?";
	}
	
	@Override
	public String getAnyClassName() {
		return FLOAT.getAnyClassName();
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
		return FLOAT;
	}
}
