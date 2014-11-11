/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import org.objectweb.asm.Label;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleNullableVirtualMethod implements ICastingRule {
	private final ZenType type;
	private final IMethod method;
	
	public CastingRuleNullableVirtualMethod(ZenType type, IMethod method) {
		this.type = type;
		this.method = method;
	}

	@Override
	public void compile(MethodOutput output) {
		Label lblNotNull = new Label();
		Label lblAfter = new Label();

		output.dup();
		output.ifNonNull(lblNotNull);
		output.pop();
		output.aConstNull();
		output.goTo(lblAfter);

		output.label(lblNotNull);
		method.invokeVirtual(output);

		output.label(lblAfter);
	}

	@Override
	public ZenType getInputType() {
		return type;
	}

	@Override
	public ZenType getResultingType() {
		return method.getReturnType();
	}
}
