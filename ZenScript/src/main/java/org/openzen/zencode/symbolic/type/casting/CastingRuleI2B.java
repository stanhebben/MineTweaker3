/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingRuleI2B extends BaseCastingRule {
	private final TypeRegistry types;
	
	public CastingRuleI2B(ICastingRule baseRule, TypeRegistry types) {
		super(baseRule);
		
		this.types = types;
	}

	@Override
	public void compileInner(MethodOutput output) {
		output.i2b();
	}

	@Override
	public ZenType getInnerInputType() {
		return types.INT;
	}

	@Override
	public ZenType getResultingType() {
		return types.BYTE;
	}
}
