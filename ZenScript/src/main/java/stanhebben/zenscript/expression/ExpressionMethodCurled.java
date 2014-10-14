/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.symbolic.method.IMethod;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionMethodCurled extends Expression {
	private final IMethod method;
	private final Expression receiver;
	
	public ExpressionMethodCurled(
			ZenPosition position,
			IScopeMethod scope,
			IMethod method,
			Expression receiver) {
		super(position, scope);
		
		this.method = method;
		this.receiver = receiver;
	}

	@Override
	public ZenType getType() {
		return method.getFunctionType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		// TODO: finish
		throw new UnsupportedOperationException("Not finished yet");
	}
}
