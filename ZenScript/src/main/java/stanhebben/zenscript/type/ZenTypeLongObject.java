/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stanhebben.zenscript.type;

import org.openzen.zencode.symbolic.scope.IScopeGlobal;
import org.openzen.zencode.java.method.JavaMethod;
import static org.openzen.zencode.runtime.IAny.NUM_LONG;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.TypeRegistry;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableStaticMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleNullableVirtualMethod;
import org.openzen.zencode.symbolic.type.casting.CastingRuleVirtualMethod;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import org.openzen.zencode.symbolic.util.CommonMethods;

/**
 *
 * @author Stan
 */
public class ZenTypeLongObject extends ZenTypeArithmeticNullable
{
	public ZenTypeLongObject(IScopeGlobal environment, ZenType LONG)
	{
		super(environment, LONG);
	}

	@Override
	public void constructCastingRules(AccessScope access, ICastingRuleDelegate rules, boolean followCasters)
	{
		TypeRegistry types = getScope().getTypes();
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

		rules.registerCastingRule(types.STRING, new CastingRuleNullableVirtualMethod(types.LONGOBJECT, methods.LONG_TOSTRING));
		rules.registerCastingRule(types.ANY, new CastingRuleNullableStaticMethod(
				JavaMethod.getStatic(getAnyClassName(), "valueOf", types.ANY, types.LONG),
				new CastingRuleVirtualMethod(this, methods.LONG_VALUE)));

		if (followCasters)
			constructExpansionCastingRules(access, rules);
	}

	@Override
	public Class toJavaClass()
	{
		return Long.class;
	}

	@Override
	public int getNumberType()
	{
		return NUM_LONG;
	}

	@Override
	public String getName()
	{
		return "long?";
	}
}
