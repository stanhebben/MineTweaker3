/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;

/**
 *
 * @author Stan
 */
public class CastingRuleDelegateStaticMethod implements ICastingRuleDelegate {
	private final ICastingRuleDelegate target;
	private final IMethod method;
	
	public CastingRuleDelegateStaticMethod(ICastingRuleDelegate target, IMethod method) {
		this.target = target;
		this.method = method;
	}

	@Override
	public void registerCastingRule(ZenType type, ICastingRule rule) {
		target.registerCastingRule(type, new CastingRuleStaticMethod(method, rule));
	}
}
