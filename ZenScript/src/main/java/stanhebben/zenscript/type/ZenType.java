package stanhebben.zenscript.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import stanhebben.zenscript.TypeExpansion;
import zenscript.annotations.CompareType;
import zenscript.annotations.OperatorType;
import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import zenscript.symbolic.type.casting.CastingRuleDelegateMap;
import zenscript.symbolic.type.casting.ICastingRule;
import zenscript.symbolic.type.casting.ICastingRuleDelegate;
import stanhebben.zenscript.type.expand.ZenExpandCaster;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

public abstract class ZenType {
	private final IScopeGlobal environment;
	private Map<ZenType, ICastingRule> castingRules = null;
	
	public ZenType(IScopeGlobal environment) {
		this.environment = environment;
	}
	
	public abstract Expression unary(
			ZenPosition position,
			IScopeMethod environment,
			Expression value,
			OperatorType operator);
	
	public abstract Expression binary(
			ZenPosition position,
			IScopeMethod environment,
			Expression left,
			Expression right,
			OperatorType operator);
	
	public abstract Expression trinary(
			ZenPosition position,
			IScopeMethod environment,
			Expression first,
			Expression second,
			Expression third,
			OperatorType operator);
	
	public abstract Expression compare(
			ZenPosition position,
			IScopeMethod environment,
			Expression left,
			Expression right,
			CompareType type);
	
	public abstract IPartialExpression getMember(
			ZenPosition position,
			IScopeMethod environment,
			IPartialExpression value,
			String name);
	
	public abstract IPartialExpression getStaticMember(
			ZenPosition position,
			IScopeMethod environment,
			String name);
	
	public abstract void constructCastingRules(ICastingRuleDelegate rules, boolean followCasters);
	
	public abstract IZenIterator makeIterator(int numValues);
	
	public ICastingRule getCastingRule(ZenType type) {
		if (castingRules == null) {
			castingRules = new HashMap<ZenType, ICastingRule>();
			constructCastingRules(new CastingRuleDelegateMap(this, castingRules), true);
		}
		
		return castingRules.get(type);
	}
	
	public final boolean canCastImplicit(ZenType type) {
		if (equals(type))
			return true;
		
		return getCastingRule(type) != null;
	}

	public boolean canCastExplicit(ZenType type) {
		return canCastImplicit(type);
	}
	
	public abstract Class toJavaClass();
	
	public abstract Type toASMType();
	
	public abstract int getNumberType();
	
	public abstract String getSignature();
	
	public abstract boolean isNullable();
	
	public abstract String getAnyClassName();
	
	public abstract String getName();
	
	public abstract Expression defaultValue(ZenPosition position, IScopeMethod environment);
	
	public boolean isLarge() {
		return false;
	}
	
	public abstract ZenType nullable();
	
	public abstract ZenType nonNull();
	
	public List<IMethod> getMethods() {
		return Collections.EMPTY_LIST;
	}
	
	public List<IMethod> getConstructors() {
		return Collections.EMPTY_LIST;
	}
	
	public IMethod getFunction() {
		return null;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	protected IScopeGlobal getEnvironment() {
		return environment;
	}
	
	protected Expression unaryExpansion(
			ZenPosition position,
			IScopeMethod environment,
			Expression value,
			OperatorType operator) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			return expansion.unary(position, environment, value, operator);
		}
		return null;
	}
	
	protected Expression binaryExpansion(
			ZenPosition position,
			IScopeMethod environment,
			Expression left,
			Expression right,
			OperatorType operator) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			return expansion.binary(position, environment, left, right, operator);
		}
		return null;
	}
	
	protected Expression trinaryExpansion(
			ZenPosition position,
			IScopeMethod environment,
			Expression first,
			Expression second,
			Expression third,
			OperatorType operator) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			return expansion.ternary(position, environment, first, second, third, operator);
		}
		return null;
	}
	
	protected IPartialExpression memberExpansion(
			ZenPosition position,
			IScopeMethod environment,
			Expression value,
			String member) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			return expansion.instanceMember(position, environment, value, member);
		}
		return null;
	}
	
	protected IPartialExpression staticMemberExpansion(
			ZenPosition position,
			IScopeMethod environment,
			String member) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			return expansion.staticMember(position, environment, member);
		}
		return null;
	}
	
	protected void constructExpansionCastingRules(ICastingRuleDelegate rules) {
		TypeExpansion expansion = environment.getExpansion(getName());
		if (expansion != null) {
			expansion.constructCastingRules(environment, rules);
		}
	}
	
	protected boolean canCastExpansion(ZenType toType) {
		String name = getName();
		TypeExpansion expansion = environment.getExpansion(name);
		if (expansion != null) {
			ZenExpandCaster caster = expansion.getCaster(toType, environment);
			if (caster != null) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ZenType) {
			return getName().equals(((ZenType) other).getName());
		} else {
			return false;
		}
	}
}
