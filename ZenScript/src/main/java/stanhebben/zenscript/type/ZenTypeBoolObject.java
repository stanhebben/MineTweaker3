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
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.method.JavaMethod;
import static org.openzen.zencode.util.ZenTypeUtil.signature;
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
public class ZenTypeBoolObject extends ZenType {
	private final ZenType BOOL;
	
	public ZenTypeBoolObject(IScopeGlobal environment, ZenType BOOL) {
		super(environment);
		
		this.BOOL = BOOL;
	}
	
	@Override
	public Expression operator(CodePosition position, IScopeMethod scope, OperatorType operation, Expression... values)
	{
		values[0] = values[0].cast(position, BOOL);
		return BOOL.operator(position, scope, operation, values);
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		return BOOL.compare(position, environment, left.cast(position, BOOL), right, type);
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod environment, IPartialExpression value, String name) {
		return BOOL.getMember(position, environment, value.eval().cast(position, BOOL), name);
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		return BOOL.getStaticMember(position, environment, name);
	}
	
	@Override
	public IZenIterator makeIterator(int numValues) {
		return BOOL.makeIterator(numValues);
	}
	
	@Override
	public void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters)
	{
		TypeRegistry types = getScope().getTypes();
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
	public boolean canCastExplicit(AccessScope accessScope, ZenType type) {
		return canCastImplicit(accessScope, type);
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
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
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
