/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.type.natives;

import java.util.List;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.type.ZenTypeFunction;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.symbolic.method.AbstractMethod;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;

/**
 * This method, when called, will add an expression as first argument to
 * another method.
 * 
 * @author Stan Hebben
 */
public class JavaMethodPrefixed extends AbstractMethod {
	private final Expression prefix;
	private final IMethod baseMethod;
	private final ZenTypeFunction functionType;
	
	public JavaMethodPrefixed(Expression prefix, IMethod baseMethod) {
		this.prefix = prefix;
		this.baseMethod = baseMethod;
		
		MethodHeader baseHeader = baseMethod.getMethodHeader();
		List<MethodParameter> newArguments = baseHeader.getArguments().subList(1, baseHeader.getArguments().size());
		functionType = new ZenTypeFunction(new MethodHeader(baseHeader.getReturnType(), newArguments, baseHeader.isVarargs()));
	}

	@Override
	public boolean isStatic() {
		return baseMethod.isStatic();
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
	public ZenTypeFunction getFunctionType()
	{
		return functionType;
	}
}
