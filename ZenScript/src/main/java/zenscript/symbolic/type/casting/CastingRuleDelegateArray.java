/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import stanhebben.zenscript.compiler.IScopeGlobal;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayBasic;
import stanhebben.zenscript.type.ZenTypeArrayList;

/**
 *
 * @author Stan
 */
public class CastingRuleDelegateArray implements ICastingRuleDelegate {
	private final IScopeGlobal environment;
	private final ICastingRuleDelegate base;
	private final ZenTypeArrayBasic from;
	
	public CastingRuleDelegateArray(IScopeGlobal environment, ICastingRuleDelegate base, ZenTypeArrayBasic from) {
		this.environment = environment;
		this.base = base;
		this.from = from;
	}

	@Override
	public void registerCastingRule(ZenType type, ICastingRule rule) {
		base.registerCastingRule(new ZenTypeArrayBasic(type), new CastingRuleArrayArray(rule, from, new ZenTypeArrayBasic(type)));
		base.registerCastingRule(new ZenTypeArrayList(type), new CastingRuleArrayList(rule, from, new ZenTypeArrayList(type)));
	}
}
