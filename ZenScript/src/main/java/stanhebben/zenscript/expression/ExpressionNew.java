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
 * @author Stan
 */
public class ExpressionNew extends Expression {
	private final ZenType type;
	private final IMethod constructor;
	private final Expression[] arguments;
	
	public ExpressionNew(ZenPosition position, IScopeMethod environment, ZenType type, IMethod constructor, Expression[] arguments) {
		super(position, environment);
		
		this.type = type;
		this.constructor = constructor;
		this.arguments = arguments;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		output.newObject(type.toASMType());
		
		if (result)
			output.dup();
		
		for (Expression argument : arguments) {
			argument.compile(true, output);
		}
		
		constructor.invokeSpecial(output);
	}

	@Override
	public ZenType getType() {
		return type;
	}
}
