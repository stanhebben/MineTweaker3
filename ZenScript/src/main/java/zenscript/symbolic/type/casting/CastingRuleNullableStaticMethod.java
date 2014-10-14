/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import org.objectweb.asm.Label;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleNullableStaticMethod implements ICastingRule {
	private final IMethod method;
	private final ICastingRule base;
	
	public CastingRuleNullableStaticMethod(IMethod method) {
		this.method = method;
		base = null;
	}
	
	public CastingRuleNullableStaticMethod(IMethod method, ICastingRule base) {
		this.method = method;
		this.base = base;
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
		
		if (base != null) {
			base.compile(output);
		}

		this.method.invokeStatic(output);

		output.label(lblAfter);
	}

	@Override
	public ZenType getInputType() {
		return method.getMethodHeader().getArguments().get(0).getType();
	}

	@Override
	public ZenType getResultingType() {
		return method.getReturnType();
	}
}
