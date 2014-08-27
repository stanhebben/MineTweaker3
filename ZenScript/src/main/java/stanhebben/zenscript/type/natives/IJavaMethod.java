/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;

/**
 *
 * @author Stan
 */
public interface IJavaMethod {
	public boolean isStatic();
	
	public boolean isVarargs();
	
	public boolean accepts(int numArguments);
	
	public void invokeVirtual(MethodOutput output);
	
	public void invokeStatic(MethodOutput output);
	
	public void invokeSpecial(MethodOutput output);
	
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments);
	
	public void invokeStatic(MethodOutput output, Expression[] arguments);
	
	public JavaMethodArgument[] getArguments();
	
	public int getArgumentIndex(String name);
	
	public ZenType getReturnType();
}
