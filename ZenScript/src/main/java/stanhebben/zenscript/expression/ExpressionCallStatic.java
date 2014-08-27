/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionCallStatic extends Expression {
	private final IJavaMethod method;
	private final Expression[] arguments;
	
	public ExpressionCallStatic(
			ZenPosition position,
			IScopeMethod environment,
			IJavaMethod method,
			Expression... arguments) {
		super(position, environment);
		
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public ZenType getType() {
		return method.getReturnType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		method.invokeStatic(output, arguments);
		
		if (method.getReturnType() != getEnvironment().getTypes().VOID && !result) {
			output.pop(method.getReturnType().isLarge());
		}
	}
}
