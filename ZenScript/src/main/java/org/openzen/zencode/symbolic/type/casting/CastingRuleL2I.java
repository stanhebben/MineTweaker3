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
public class CastingRuleL2I extends BaseCastingRule {
	private final TypeRegistry types;
	
	public CastingRuleL2I(ICastingRule baseRule, TypeRegistry types) {
		super(baseRule);
		
		this.types = types;
	}

	@Override
	public void compileInner(MethodOutput output) {
		output.l2i();
	}

	@Override
	public ZenType getInnerInputType() {
		return types.LONG;
	}

	@Override
	public ZenType getResultingType() {
		return types.INT;
	}
}
