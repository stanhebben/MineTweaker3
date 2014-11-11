/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.TypeRegistry;

/**
 *
 * @author Stan
 */
public class CastingAnyByte implements ICastingRule {
	private final TypeRegistry types;
	
	public CastingAnyByte(TypeRegistry types) {
		this.types = types;
	}
	
	@Override
	public void compile(MethodOutput output) {
		output.invokeInterface(IAny.class, "asByte", byte.class);
	}
	
	@Override
	public ZenType getInputType() {
		return types.ANY;
	}
	
	@Override
	public ZenType getResultingType() {
		return types.BYTE;
	}
}
