package stanhebben.zenscript.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.type.TypeExpansion;
import org.openzen.zencode.annotations.CompareType;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.type.casting.CastingRuleDelegateMap;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.MemberStatic;
import org.openzen.zencode.symbolic.MemberVirtual;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.CodePosition;

public abstract class ZenType
{
	public static final AccessScope ACCESS_GLOBAL = AccessScope.createModuleScope();
	
	private final IScopeGlobal scope;
	private final Map<AccessScope, Map<ZenType, ICastingRule>> castingRules = new HashMap<AccessScope, Map<ZenType, ICastingRule>>();
	private final List<TypeExpansion> expansions = new ArrayList<TypeExpansion>();

	public ZenType(IScopeGlobal environment)
	{
		this.scope = environment;
	}

	public IScopeGlobal getScope()
	{
		return scope;
	}
	
	public TypeRegistry getTypeRegistry()
	{
		return scope.getTypes();
	}

	public void addExpansion(TypeExpansion expansion)
	{
		expansions.add(expansion);
	}
	
	public List<TypeExpansion> getExpansions()
	{
		return expansions;
	}

	public abstract Expression operator(
			CodePosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values);

	public abstract Expression compare(
			CodePosition position,
			IScopeMethod environment,
			Expression left,
			Expression right,
			CompareType type);

	public abstract IPartialExpression getMember(
			CodePosition position,
			IScopeMethod environment,
			IPartialExpression value,
			String name);

	public abstract IPartialExpression getStaticMember(
			CodePosition position,
			IScopeMethod environment,
			String name);

	public abstract void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters);

	public abstract IZenIterator makeIterator(int numValues);

	public ICastingRule getCastingRule(AccessScope scope, ZenType type)
	{
		if (!castingRules.containsKey(scope)) {
			castingRules.put(scope, new HashMap<ZenType, ICastingRule>());
			constructCastingRules(scope, new CastingRuleDelegateMap(this, castingRules.get(scope)), true);
		}

		return castingRules.get(scope).get(type);
	}

	public final boolean canCastImplicit(AccessScope scope, ZenType type)
	{
		if (equals(type))
			return true;

		return getCastingRule(scope, type) != null;
	}

	public boolean canCastExplicit(AccessScope scope, ZenType type)
	{
		return canCastImplicit(scope, type);
	}

	public abstract Class toJavaClass();

	public abstract Type toASMType();

	public abstract int getNumberType();

	public abstract String getSignature();

	public abstract boolean isNullable();

	public abstract String getAnyClassName();

	public abstract String getName();

	public abstract Expression defaultValue(CodePosition position, IScopeMethod environment);

	public boolean isLarge()
	{
		return false;
	}

	public abstract ZenType nullable();

	public abstract ZenType nonNull();

	public List<IMethod> getMethods()
	{
		return Collections.EMPTY_LIST;
	}

	public List<IMethod> getConstructors()
	{
		return Collections.EMPTY_LIST;
	}

	public IMethod getFunction()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	protected Expression expandOperator(
			CodePosition position,
			IScopeMethod environment,
			OperatorType operator,
			Expression... values)
	{
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

	protected void memberExpansion(MemberVirtual member)
	{
		for (TypeExpansion expansion : expansions) {
			if (expansion.isVisibleTo(member.getScope().getAccessScope()))
				expansion.expandMember(member);
		}
	}

	protected void staticMemberExpansion(MemberStatic member)
	{
		for (TypeExpansion expansion : expansions) {
			if (expansion.isVisibleTo(member.getScope().getAccessScope()))
				expansion.expandStaticMember(member);
		}
	}

	protected void constructExpansionCastingRules(AccessScope accessScope, ICastingRuleDelegate rules)
	{
		for (TypeExpansion expansion : expansions) {
			if (expansion.isVisibleTo(accessScope))
				expansion.expandCastingRules(rules);
		}
	}

	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof ZenType)
			return getName().equals(((ZenType) other).getName());
		else
			return false;
	}
}
