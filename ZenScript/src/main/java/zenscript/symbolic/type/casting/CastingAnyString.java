/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import org.objectweb.asm.Label;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.runtime.IAny;
import zenscript.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingAnyString implements ICastingRule {
	private final TypeRegistry types;
	
	public CastingAnyString(TypeRegistry types) {
		this.types = types;
	}
	
	@Override
	public void compile(MethodOutput output) {
		Label lblNonNull = new Label();
		Label lblAfter = new Label();
		
		output.dup();
		output.ifNonNull(lblNonNull);
		output.pop();
		output.aConstNull();
		output.goTo(lblAfter);
		
		output.label(lblNonNull);
		output.invokeInterface(IAny.class, "asString", String.class);
		
		output.label(lblAfter);
	}

	@Override
	public ZenType getInputType() {
		return types.ANY;
	}

	@Override
	public ZenType getResultingType() {
		return types.STRING;
	}
}
