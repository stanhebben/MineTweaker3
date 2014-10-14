/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type;

import java.util.Map;
import org.objectweb.asm.Type;
import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeMethod;
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
import zenscript.symbolic.TypeRegistry;
import zenscript.symbolic.type.casting.CastingRuleMap;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import zenscript.util.ZenPosition;

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
	public void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters) {
		if (followCasters) {
			constructExpansionCastingRules(rules);
		}
	}
	
	@Override
	public ICastingRule getCastingRule(ZenType type) {
		TypeRegistry types = getScope().getTypes();
		
		ICastingRule base = super.getCastingRule(type);
		if (base == null && type instanceof ZenTypeAssociative && keyType == types.ANY && valueType == types.ANY) {
			ZenTypeAssociative aType = (ZenTypeAssociative) type;
			return new CastingRuleMap(
					types.ANY.getCastingRule(aType.keyType),
					types.ANY.getCastingRule(aType.valueType),
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
	public Expression operator(ZenPosition position, IScopeMethod environment, OperatorType operator, Expression... values) {
		Expression result = expandOperator(position, environment, operator, values);
		if (result == null) {
			environment.error(position, "associative array doesn't have this operator");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	@Override
	public Expression compare(ZenPosition position, IScopeMethod environment, Expression left, Expression right, CompareType type) {
		Expression result = operator(position, environment, OperatorType.COMPARE, left, right);
		if (result == null) {
			environment.error(position, "cannot compare associative arrays");
			return new ExpressionInvalid(position, environment);
		} else {
			return new ExpressionCompareGeneric(position, environment, result, type);
		}
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IScopeMethod environment, IPartialExpression value, String name) {
		TypeRegistry types = getScope().getTypes();
		
		if (name.equals("length")) {
			return new ExpressionMapSize(position, environment, value.eval());
		} else if (types.STRING.canCastImplicit(keyType)) {
			return new ExpressionMapIndexGet(
					position,
					environment,
					value.eval(),
					new ExpressionString(position, environment, name).cast(position, keyType));
		} else {
			IPartialExpression result = memberExpansion(position, environment, value.eval(), name);
			if (result == null) {
				environment.error(position, "this array is not indexable with strings");
				return new ExpressionInvalid(position, environment, valueType);
			} else {
				return result;
			}
		}
	}

	@Override
	public IPartialExpression getStaticMember(ZenPosition position, IScopeMethod environment, String name) {
		IPartialExpression result = staticMemberExpansion(position, environment, name);
		if (result == null) {
			environment.error(position, "associative arrays don't have static members");
			return new ExpressionInvalid(position, environment);
		} else {
			return result;
		}
	}

	/*@Override
	public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression receiver, Expression... arguments) {
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
	public boolean canCastExplicit(ZenType type) {
		return type == this || canCastAssociative(type) || canCastExpansion(type);
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
	public Expression defaultValue(ZenPosition position, IScopeMethod environment) {
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
	
	private boolean canCastAssociative(ZenType type) {
		if (!(type instanceof ZenTypeAssociative)) {
			return false;
		}
		
		ZenTypeAssociative atype = (ZenTypeAssociative) type;
		return getKeyType().canCastImplicit(atype.getKeyType())
				&& getValueType().canCastImplicit(atype.getValueType());
	}
}
