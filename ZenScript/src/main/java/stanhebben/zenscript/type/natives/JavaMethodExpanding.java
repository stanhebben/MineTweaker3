/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.symbolic.method.AbstractMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 * An expanding method is a static method that acts as a virtual method for an
 * existing class. The "this" parameter becomes the first parameter for the 
 * static method.
 * 
 * @author Stan Hebben
 */
public class JavaMethodExpanding extends AbstractMethod {
	private final IMethod baseMethod;
	private final ZenTypeFunction functionType;
	
	public JavaMethodExpanding(IMethod baseMethod) {
		this.baseMethod = baseMethod;
		
		MethodHeader originalHeader = baseMethod.getMethodHeader();
		
		List<MethodParameter> arguments = baseMethod.getMethodHeader().getArguments().subList(
				1,
				baseMethod.getMethodHeader().getArguments().size());
		MethodHeader newHeader = new MethodHeader(originalHeader.getReturnType(), arguments, originalHeader.isVarargs());
		
		functionType = new ZenTypeFunction(newHeader);
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public void invokeVirtual(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call expansion methods directly");
	}

	@Override
	public void invokeStatic(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call expansion methods statically");
	}
	
	@Override
	public void invokeSpecial(MethodOutput output) {
		throw new UnsupportedOperationException("Cannot call expansion methods directly");
	}

	@Override
	public void invokeVirtual(MethodOutput output, Expression receiver, Expression[] arguments) {
		Expression[] newArguments = new Expression[arguments.length + 1];
		newArguments[0] = receiver;
		System.arraycopy(arguments, 0, newArguments, 1, arguments.length);
		
		baseMethod.invokeStatic(output, newArguments);
	}

	@Override
	public void invokeStatic(MethodOutput output, Expression[] arguments) {
		throw new UnsupportedOperationException("Cannot call expansion methods statically");
	}

	@Override
	public ZenTypeFunction getFunctionType()
	{
		return functionType;
	}
}
