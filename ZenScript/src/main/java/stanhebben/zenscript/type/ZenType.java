package stanhebben.zenscript.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import zenscript.symbolic.type.TypeExpansion;
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
import zenscript.symbolic.MemberVirtual;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

public abstract class ZenType {
	private final IScopeGlobal scope;
	private Map<ZenType, ICastingRule> castingRules = null;
	private final List<TypeExpansion> expansions = new ArrayList<TypeExpansion>();
	
	public ZenType(IScopeGlobal environment) {
		this.scope = environment;
	}
	
	public IScopeGlobal getScope() {
		return scope;
	}
	
	public void addExpansion(TypeExpansion expansion)
	{
		expansions.add(expansion);
	}
	
	public abstract Expression operator(
			ZenPosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values);
	
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
	
	protected Expression expandOperator(
			ZenPosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values) {
		for (TypeExpansion expansion : expansions) {
			Expression expansionOperator = expansion.operatorExact(position, environment, operator, values);
			if (expansionOperator != null)
				return expansionOperator;
		}
		
		for (TypeExpansion expansion : expansions) {
			Expression expansionOperator = expansion.operator(position, environment, operator, values);
			if (expansionOperator != null)
				return expansionOperator;
		}
		
		return null;
	}
	
	protected void memberExpansion(MemberVirtual member) {
		for (TypeExpansion expansion : expansions) {
			if (expansion.getScope().isVisible(member.getScope()))
				expansion.expandMember(member);
		}
	}
	
	protected void staticMemberExpansion(MemberStatic member) {
		for (TypeExpansion expansion : expansions) {
			if (expansion.getScope().isVisible(member.getScope()))
				expansion.expandStaticMember(member);
		}
	}
	
	protected void constructExpansionCastingRules(ICastingRuleDelegate rules) {
		TypeExpansion expansion = scope.getExpansion(getName());
		if (expansion != null) {
			expansion.constructCastingRules(scope, rules);
		}
	}
	
	protected boolean canCastExpansion(ZenType toType) {
		String name = getName();
		TypeExpansion expansion = scope.getExpansion(name);
		if (expansion != null) {
			ZenExpandCaster caster = expansion.getCaster(toType, scope);
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
