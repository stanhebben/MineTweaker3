/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.method.JavaMethod;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import static org.openzen.zencode.runtime.IAny.NUM_SHORT;
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
public class ZenTypeShortObject extends ZenType {
	private final ZenType SHORT;
	
	public ZenTypeShortObject(IScopeGlobal environment, ZenType SHORT) {
		super(environment);
		
		this.SHORT = SHORT;
	}

	@Override
	public Expression unary(CodePosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		return SHORT.unary(position, environment, value.cast(position, SHORT), operator);
	}

	@Override
	public Expression binary(CodePosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		return SHORT.binary(position, environment, left.cast(position, SHORT), right, operator);
	}

	@Override
	public Expression trinary(CodePosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		return SHORT.trinary(position, environment, first.cast(position, SHORT), second, third, operator);
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return SHORT.compare(position, environment, left.cast(position, SHORT), right, type);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return SHORT.getMember(position, environment, value.eval().cast(position, SHORT), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		return SHORT.getStaticMember(position, environment, name);
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		return SHORT.call(position, environment, receiver.cast(position, SHORT), arguments);
	}*/

	@Override
	public IZenIterator makeIterator(int numValues) {
		return SHORT.makeIterator(numValues);
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
		
		rules.registerCastingRule(types.STRING, new CastingRuleNullableVirtualMethod(types.SHORTOBJECT, methods.SHORT_TOSTRING));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(
				JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, SHORT),
				new CastingRuleVirtualMethod(this, methods.SHORT_VALUE)));
		
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}

	@Override
	public Class toJavaClass() {
		return Short.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(Short.class);
	}

	@Override
	public int getNumberType() {
		return NUM_SHORT;
	}

	@Override
	public String getSignature() {
		return signature(Short.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return "short";
	}
	
	@Override
	public String getAnyClassName() {
		return SHORT.getAnyClassName();
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
