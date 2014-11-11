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
public class ExpressionCallStatic extends Expression {
	private final IMethod method;
	private final Expression[] arguments;
	
	public ExpressionCallStatic(
			CodePosition position,
			IScopeMethod scope,
			IMethod method,
			Expression... arguments) {
		super(position, scope);
		
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public ZenType getType() {
		return method.getMethodHeader().getReturnType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		method.invokeStatic(output, arguments);
		
		if (method.getMethodHeader().getReturnType() != getScope().getTypes().VOID && !result) {
			output.pop(method.getMethodHeader().getReturnType().isLarge());
		}
	}
}
