/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import org.objectweb.asm.Label;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingNotNull implements ICastingRule {
	private final ZenType fromType;
	private final ZenType bool;
	
	public CastingNotNull(ZenType fromType, ZenType bool) {
		this.fromType = fromType;
		this.bool = bool;
	}

	@Override
	public void compile(MethodOutput output) {
		Label labelElse = new Label();
		Label labelAfter = new Label();
		
		output.ifNull(labelElse);
		output.iConst1();
		output.goTo(labelAfter);
		output.label(labelElse);
		output.iConst0();
		output.label(labelAfter);
	}

	@Override
	public ZenType getInputType() {
		return fromType;
	}

	@Override
	public ZenType getResultingType() {
		return bool;
	}
}
