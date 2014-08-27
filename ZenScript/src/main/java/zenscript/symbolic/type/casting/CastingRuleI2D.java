/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingRuleI2D extends BaseCastingRule {
	private final TypeRegistry types;
	
	public CastingRuleI2D(ICastingRule baseRule, TypeRegistry types) {
		super(baseRule);
		
		this.types = types;
	}

	@Override
	public void compileInner(MethodOutput output) {
		output.i2d();
	}

	@Override
	public ZenType getInnerInputType() {
		return types.INT;
	}

	@Override
	public ZenType getResultingType() {
		return types.DOUBLE;
	}
}
