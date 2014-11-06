/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import java.util.Map;
import org.objectweb.asm.Type;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionCompareGeneric;
import stanhebben.zenscript.expression.ExpressionInvalid;
import stanhebben.zenscript.expression.ExpressionMapIndexGet;
import stanhebben.zenscript.expression.ExpressionMapSize;
import stanhebben.zenscript.expression.ExpressionNull;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.type.iterator.IteratorMap;
import stanhebben.zenscript.type.iterator.IteratorMapKeys;
import static stanhebben.zenscript.util.ZenTypeUtil.signature;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleMap;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ZenTypeAssociative extends ZenType {
	private final ZenType valueType;
	private final ZenType keyType;
	
	private final String name;
	
	public ZenTypeAssociative(ZenType valueType, ZenType keyType) {
		super(valueType.getScope());
		
		this.valueType = valueType;
		this.keyType = keyType;
		
		name = valueType.getName() + "[" + keyType.getName() + "]";
	}
	
	public ZenType getValueType() {
		return valueType;
	}
	
	public ZenType getKeyType() {
		return keyType;
	}
	
	@Override
	public void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters) {
		if (followCasters) {
			constructExpansionCastingRules(accessScope, rules);
		}
	}
	
	@Override
	public ICastingRule getCastingRule(AccessScope accessScope, ZenType type) {
		TypeRegistry types = getScope().getTypes();
		
		ICastingRule base = super.getCastingRule(accessScope, type);
		if (base == null && type instanceof ZenTypeAssociative && keyType == types.ANY && valueType == types.ANY) {
			ZenTypeAssociative aType = (ZenTypeAssociative) type;
			return new CastingRuleMap(
					types.ANY.getCastingRule(accessScope, aType.keyType),
					types.ANY.getCastingRule(accessScope, aType.valueType),
					this,
					aType);
		} else {
			return base;
		}
	}
	
	@Override
	public String getAnyClassName() {
		return null;
	}

	@Override
	public Expression operator(CodePosition position, IScopeMethod environment, OperatorType operator, Expression... values) {
		Expression result = expandOperator(position, environment, operator, values);
		if (result == null) {
			environment.error(position, "associative array doesn't have this operator");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public Expression compare(CodePosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		Expression result = operator(position, environment, OperatorType.COMPARE, left, right);
		if (result == null) {
			environment.error(position, "cannot compare associative arrays");
			return new ExpressionInvalid(position, environment);
		} else {
			return new ExpressionCompareGeneric(position, environment, result, type);
		}
	}

	@Override
	public IPartialExpression getMember(CodePosition position, IScopeMethod scope, IPartialExpression value, String name) {
		TypeRegistry types = getScope().getTypes();
		
		if (name.equals("length")) {
			return new ExpressionMapSize(position, scope, value.eval());
		} else if (types.STRING.canCastImplicit(scope.getAccessScope(), keyType)) {
			return new ExpressionMapIndexGet(
					position,
					scope,
					value.eval(),
					new ExpressionString(position, scope, name).cast(position, keyType));
		} else {
			MemberVirtual result = new MemberVirtual(position, scope, value.eval(), name);
			memberExpansion(result);
			if (result.isEmpty()) {
				scope.error(position, "this array is not indexable with strings");
				return new ExpressionInvalid(position, scope, valueType);
			} else {
				return result;
			}
		}
	}

	@Override
	public IPartialExpression getStaticMember(CodePosition position, IScopeMethod environment, String name) {
		MemberStatic result = new MemberStatic(position, environment, valueType, name);
		staticMemberExpansion(result);
		if (result == null) {
			environment.error(position, "associative arrays don't have static members");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	/*@Override
	public Expression call(CodePosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
		environment.error(position, "cannot call associative arrays");
		return new ExpressionInvalid(position);
	}
	
	@Override
	public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
		if (followCasters) {
			constructExpansionCastingRules(environment, rules);
		}
	}*/
	
	@Override
	public IZenIterator makeIterator(int numValues) {
		if (numValues == 1) {
			return new IteratorMapKeys(this);
		} else if (numValues == 2) {
			return new IteratorMap(this);
		} else {
			return null;
		}
	}

	@Override
	public boolean canCastExplicit(AccessScope accessScope, ZenType type) {
		return type == this || canCastImplicit(accessScope, type) || canCastAssociative(accessScope, type);
	}

	@Override
	public Class toJavaClass() {
		return Map.class;
	}

	@Override
	public Type toASMType() {
		return Type.getType(Map.class);
	}

	@Override
	public int getNumberType() {
		return 0;
	}

	@Override
	public String getSignature() {
		return signature(Map.class);
	}

	@Override
	public boolean isNullable() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Expression defaultValue(CodePosition position, IScopeMethod environment) {
		return new ExpressionNull(position, environment);
	}
	
	@Override
	public ZenType nonNull() {
		return this;
	}
	
	@Override
	public ZenType nullable() {
		return this;
	}
	
	private boolean canCastAssociative(AccessScope accessScope, ZenType type) {
		if (!(type instanceof ZenTypeAssociative)) {
			return false;
		}
		
		ZenTypeAssociative atype = (ZenTypeAssociative) type;
		return getKeyType().canCastImplicit(accessScope, atype.getKeyType())
				&& getValueType().canCastImplicit(accessScope, atype.getValueType());
	}
}
