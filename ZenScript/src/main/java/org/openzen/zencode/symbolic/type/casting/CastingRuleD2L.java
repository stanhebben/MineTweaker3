/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingRuleD2L extends BaseCastingRule {
	private final TypeRegistry types;
	
	public CastingRuleD2L(ICastingRule baseRule, TypeRegistry types) {
		super(baseRule);
		
		this.types = types;
	}

	@Override
	public void compileInner(MethodOutput method) {
		method.d2l();
	}

	@Override
	public ZenType getInnerInputType() {
		return types.DOUBLE;
	}

	@Override
	public ZenType getResultingType() {
		return types.LONG;
	}
}
