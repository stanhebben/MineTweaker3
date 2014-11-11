/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.symbolic.method.IMethod;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionCallVirtual extends Expression {
	private final IMethod method;
	
	private final Expression receiver;
	private final Expression[] arguments;
	
	public ExpressionCallVirtual(
			CodePosition position,
			IScopeMethod environment,
			IMethod method,
			Expression receiver,
			Expression... arguments) {
		super(position, environment);
		
		this.method = method;
		
		this.receiver = receiver;
		this.arguments = arguments;
	}

	@Override
	public ZenType getType() {
		return method.getMethodHeader().getReturnType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		method.invokeVirtual(output, receiver, arguments);
		
		if (method.getMethodHeader().getReturnType() != getScope().getTypes().VOID && !result) {
			output.pop(method.getMethodHeader().getReturnType().isLarge());
		}
	}
}
