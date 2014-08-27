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
 * An expanding method is a static method that acts as a virtual method for an
 * existing class. The "this" parameter becomes the first parameter for the 
 * static method.
 * 
 * @author Stan Hebben
 */
public class JavaMethodPrefixed implements IJavaMethod {
	private final Expression prefix;
	private final IJavaMethod baseMethod;
	private final JavaMethodArgument[] arguments;
	
	public JavaMethodPrefixed(Expression prefix, IJavaMethod baseMethod) {
		this.prefix = prefix;
		this.baseMethod = baseMethod;
		
		arguments = new JavaMethodArgument[baseMethod.getArguments().length + 1];
		System.arraycopy(baseMethod.getArguments(), 0, arguments, 1, baseMethod.getArguments().length);
		arguments[0] = new JavaMethodArgument(null, prefix.getType(), null);
	}

	@Override
	public boolean isStatic() {
		return baseMethod.isStatic();
	}

	@Override
	public boolean accepts(int numArguments) {
		return baseMethod.accepts(numArguments - 1);
	}
	
	@Override
	public void invokeVirtual(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call prefixed methods directly");
	}
	
	@Override
	public void invokeStatic(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call prefixed methods directly");
	}
	
	@Override
	public void invokeSpecial(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call prefixed methods directly");
	}

	@Override
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments) {
		Expression[] newArguments = new Expression[arguments.length + 1];
		arguments[0] = prefix;
		System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
		
		baseMethod.invokeVirtual(output, receiver, newArguments);
	}

	@Override
	public void invokeStatic(MethodOutput output, Expression[] arguments) {
		Expression[] newArguments = new Expression[arguments.length + 1];
		arguments[0] = prefix;
		System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
		
		baseMethod.invokeStatic(output, arguments);
	}

	@Override
	public JavaMethodArgument[] getArguments() {
		return arguments;
	}

	@Override
	public int getArgumentIndex(String name) {
		return baseMethod.getArgumentIndex(name) + 1;
	}

	@Override
	public ZenType getReturnType() {
		return baseMethod.getReturnType();
	}

	@Override
	public boolean isVarargs() {
		return baseMethod.isVarargs();
	}
}
