/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.symbolic.method;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public interface IMethod
{
	public Expression call(CodePosition position, IScopeMethod scope, Expression... arguments);
	
	public boolean isStatic();
	
	public void invokeVirtual(MethodOutput output);
	
	public void invokeStatic(MethodOutput output);
	
	public void invokeSpecial(MethodOutput output);
	
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments);
	
	public void invokeStatic(MethodOutput output, Expression[] arguments);
	
	public ZenTypeFunction getFunctionType();
	
	public MethodHeader getMethodHeader();
	
	public ZenType getReturnType();
}
