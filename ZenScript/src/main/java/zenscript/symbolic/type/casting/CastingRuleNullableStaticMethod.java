/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import org.objectweb.asm.Label;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleNullableStaticMethod implements ICastingRule {
	private final IJavaMethod method;
	private final ICastingRule base;
	
	public CastingRuleNullableStaticMethod(IJavaMethod method) {
		this.method = method;
		base = null;
	}
	
	public CastingRuleNullableStaticMethod(IJavaMethod method, ICastingRule base) {
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
		return method.getArguments()[0].getType();
	}

	@Override
	public ZenType getResultingType() {
		return method.getReturnType();
	}
}
