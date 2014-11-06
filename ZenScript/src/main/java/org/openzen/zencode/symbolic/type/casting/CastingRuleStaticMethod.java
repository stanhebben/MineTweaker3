/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public class CastingRuleStaticMethod implements ICastingRule {
	private final IMethod method;
	private final ICastingRule base;
	
	public CastingRuleStaticMethod(IMethod method) {
		this.method = method;
		this.base = null;
	}
	
	public CastingRuleStaticMethod(IMethod method, ICastingRule base) {
		this.method = method;
		this.base = base;
	}

	@Override
	public void compile(MethodOutput method) {
		if (base != null)
			base.compile(method);
		
		this.method.invokeStatic(method);
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
