/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.type.casting;

import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface ICastingRule {
	public void compile(MethodOutput method);
	
	public ZenType getInputType();
	
	public ZenType getResultingType();
}
