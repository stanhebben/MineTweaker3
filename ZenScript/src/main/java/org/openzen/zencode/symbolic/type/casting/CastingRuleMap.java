/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleMap implements ICastingRule {
	private final ICastingRule keyRule;
	private final ICastingRule valueRule;
	private final ZenTypeAssociative fromType;
	private final ZenTypeAssociative toType;
	
	public CastingRuleMap(ICastingRule keyRule, ICastingRule valueRule, ZenTypeAssociative fromType, ZenTypeAssociative toType) {
		this.keyRule = keyRule;
		this.valueRule = valueRule;
		this.fromType = fromType;
		this.toType = toType;
	}
	
	@Override
	public void compile(MethodOutput output) {
		// TODO: implement
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public ZenType getInputType() {
		return fromType;
	}
	
	@Override
	public ZenType getResultingType() {
		return toType;
	}
}
