/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.scope;

import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.type.ZenType;

/**
 *
 * @author Stan
 */
public interface IScopeMethod extends IScopeClass
{
	public Statement getControlStatement(String label);
	
	public ZenType getReturnType();
}
