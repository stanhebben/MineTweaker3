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
 * @author Stan
 */
public class ExpressionNew extends Expression {
	private final ZenType type;
	private final IMethod constructor;
	private final Expression[] arguments;
	
	public ExpressionNew(CodePosition position, IScopeMethod environment, ZenType type, IMethod constructor, Expression[] arguments) {
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
