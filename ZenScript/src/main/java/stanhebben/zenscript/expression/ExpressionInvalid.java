/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionInvalid extends Expression {
	private final ZenType type;
	
	public ExpressionInvalid(CodePosition position, IScopeMethod environment) {
		super(position, environment);
		
		type = environment.getTypes().ANY;
	}
	
	public ExpressionInvalid(CodePosition position, IScopeMethod environment, ZenType type) {
		super(position, environment);
		
		this.type = type;
		
		// XXX: remove before release
		//throw new RuntimeException("Constructing invalid expression");
	}

	@Override
	public Expression cast(CodePosition position, ZenType type) {
		return new ExpressionInvalid(position, getScope(), type);
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			type.defaultValue(getPosition(), getScope()).compile(result, output);
		}
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output) {
		output.goTo(onElse);
	}
}
