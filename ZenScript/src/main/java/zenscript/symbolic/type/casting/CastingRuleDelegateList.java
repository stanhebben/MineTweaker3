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
public class CastingRuleDelegateList implements ICastingRuleDelegate {
	private final IScopeGlobal environment;
	private final ICastingRuleDelegate base;
	private final ZenTypeArrayList from;
	
	public CastingRuleDelegateList(IScopeGlobal environment, ICastingRuleDelegate base, ZenTypeArrayList from) {
		this.environment = environment;
		this.base = base;
		this.from = from;
	}

	@Override
	public void registerCastingRule(ZenType type, ICastingRule rule) {
		base.registerCastingRule(new ZenTypeArrayBasic(environment, type), new CastingRuleListArray(rule, from, new ZenTypeArrayBasic(environment, type)));
		base.registerCastingRule(new ZenTypeArrayList(environment, type), new CastingRuleListList(rule, from, new ZenTypeArrayList(environment, type)));
	}
}
