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
public class ExpressionNull extends Expression {
	public ExpressionNull(ZenPosition position, IScopeMethod environment) {
		super(position, environment);
	}
	
	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		if (type.isNullable()) {
			return this;
		} else {
			getEnvironment().error(position, "Cannot convert null to " + type);
			return new ExpressionInvalid(position, getEnvironment());
		}
	}

	@Override
	public ZenType getType() {
		return getEnvironment().getTypes().NULL;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			output.aConstNull();
		}
	}

	@Override
	public void compileElse(Label onElse, MethodOutput output) {
		output.goTo(onElse);
	}
}
