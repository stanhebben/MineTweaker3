/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import org.objectweb.asm.Type;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.runtime.IAny;
import zenscript.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingRuleAnyAs implements ICastingRule {
	private final ZenType type;
	private final TypeRegistry types;
	
	public CastingRuleAnyAs(ZenType type, TypeRegistry types) {
		this.type = type;
		this.types = types;
	}
	
	@Override
	public void compile(MethodOutput output) {
		output.constant(Type.getType(type.toJavaClass()));
		output.invokeInterface(IAny.class, "as", Class.class, Object.class);
	}

	@Override
	public ZenType getInputType() {
		return types.ANY;
	}

	@Override
	public ZenType getResultingType() {
		return type;
	}
}
