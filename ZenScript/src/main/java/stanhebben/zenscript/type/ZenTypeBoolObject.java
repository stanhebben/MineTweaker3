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
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.natives.JavaMethod;
import stanhebben.zenscript.util.MethodOutput;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.type.casting.CastingRuleNullableStaticMethod;
import zenscript.symbolic.type.casting.CastingRuleNullableVirtualMethod;
import zenscript.symbolic.type.casting.CastingRuleVirtualMethod;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.symbolic.util.CommonMethods;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ZenTypeBoolObject extends ZenType {
	private final ZenType BOOL;
	
	public ZenTypeBoolObject(IScopeGlobal environment, ZenType BOOL) {
		super(environment);
		
		this.BOOL = BOOL;
	}

	@Override
	public Expression unary(ZenPosition position, IScopeMethod environment, Expression value, OperatorType operator) {
		return BOOL.unary(position, environment, value.cast(position, BOOL), operator);
	}

	@Override
	public Expression binary(ZenPosition position, IScopeMethod environment, Expression left, Expression right, OperatorType operator) {
		return BOOL.binary(position, environment, left.cast(position, BOOL), right, operator);
	}

	@Override
	public Expression trinary(ZenPosition position, IScopeMethod environment, Expression first, Expression second, Expression third, OperatorType operator) {
		return BOOL.trinary(position, environment, first.cast(position, BOOL), second, third, operator);
	}

	@Override
	public Expression compare(ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return BOOL.compare(position, environment, left.cast(position, BOOL), right, type);
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return BOOL.getMember(position, environment, value.eval().cast(position, BOOL), name);
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		return BOOL.getStaticMember(position, environment, name);
	}

	/*@Override
	public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		return BOOL.call(position, environment, receiver.cast(position, environment, BOOL), arguments);
	}*/
	
	@Override
	public IZenIterator makeIterator(int numValues, MethodOutput output) {
		return BOOL.makeIterator(numValues, output);
	}
	
	@Override
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		TypeRegistry types = getEnvironment().getTypes();
		CommonMethods methods = types.getCommonMethods();
		
		rules.registerCastingRule(types.BOOL, new CastingRuleVirtualMethod(this, methods.BOOL_VALUE));
		rules.registerCastingRule(types.STRING, new CastingRuleNullableVirtualMethod(types.BOOL, methods.BOOL_TOSTRING));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(JavaMethod.getStatic(
				BOOL.getAnyClassName(),
				"valueOf",
				types.ANY,
				BOOL
		), new CastingRuleVirtualMethod(this, methods.BOOL_VALUE)));
	}

	@Override
	public boolean canCastExplicit(ZenType type) {
		return canCastImplicit(type);
	}

	@Override
	public Class toJavaClass() {
		return Boolean.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(Boolean.class);
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return signature(Boolean.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return "bool?";
	}
	
	@Override
	public String getAnyClassName() {
		return BOOL.getAnyClassName();
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
		return BOOL;
	}
}
