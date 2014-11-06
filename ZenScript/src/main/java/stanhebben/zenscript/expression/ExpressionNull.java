/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.objectweb.asm.Label;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionNull extends Expression {
	public ExpressionNull(CodePosition position, IScopeMethod environment) {
		super(position, environment);
	}
	
	@Override
	public Expression cast(CodePosition position, ZenType type) {
		if (type.isNullable()) {
			return this;
		} else {
			getScope().error(position, "Cannot convert null to " + type);
			return new ExpressionInvalid(position, getScope());
		}
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().NULL;
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
