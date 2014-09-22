/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import zenscript.symbolic.method.IMethod;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionCallVirtual extends Expression {
	private final IMethod method;
	
	private final Expression receiver;
	private final Expression[] arguments;
	
	public ExpressionCallVirtual(
			ZenPosition position,
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
		return method.getReturnType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		method.invokeVirtual(output, receiver, arguments);
		
		if (method.getReturnType() != getEnvironment().getTypes().VOID && !result) {
			output.pop(method.getReturnType().isLarge());
		}
	}
}
