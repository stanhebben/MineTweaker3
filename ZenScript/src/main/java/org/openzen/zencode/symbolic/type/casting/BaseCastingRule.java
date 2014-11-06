/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public abstract class BaseCastingRule implements ICastingRule {
	private final ICastingRule baseRule;
	
	public BaseCastingRule(ICastingRule baseRule) {
		this.baseRule = baseRule;
	}

	@Override
	public final void compile(MethodOutput method) {
		if (baseRule != null)
			baseRule.compile(method);
		
		compileInner(method);
	}
	
	@Override
	public final ZenType getInputType() {
		return baseRule == null ? getInnerInputType() : baseRule.getInputType();
	}
	
	public abstract ZenType getInnerInputType();
	
	public abstract void compileInner(MethodOutput method);
}
