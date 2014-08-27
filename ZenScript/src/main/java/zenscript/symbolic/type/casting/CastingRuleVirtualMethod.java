/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleVirtualMethod implements ICastingRule {
	private final ZenType type;
	private final IJavaMethod method;
	
	public CastingRuleVirtualMethod(ZenType type, IJavaMethod method) {
		this.type = type;
		this.method = method;
	}

	@Override
	public void compile(MethodOutput method) {
		this.method.invokeVirtual(method);
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
