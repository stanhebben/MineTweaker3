/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionInvalid extends Expression {
	private final ZenType type;
	
	public ExpressionInvalid(ZenPosition position, IScopeMethod environment) {
		super(position, environment);
		
		type = environment.getTypes().ANY;
	}
	
	public ExpressionInvalid(ZenPosition position, IScopeMethod environment, ZenType type) {
		super(position, environment);
		
		this.type = type;
		
		// XXX: remove before release
		//throw new RuntimeException("Constructing invalid expression");
	}

	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		return new ExpressionInvalid(position, getEnvironment(), type);
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			type.defaultValue(getPosition(), getEnvironment()).compile(result, output);
		}
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output) {
		output.goTo(onElse);
	}
}
