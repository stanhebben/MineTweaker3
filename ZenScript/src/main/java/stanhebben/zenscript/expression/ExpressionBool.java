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
public class ExpressionBool extends Expression {
	private final boolean value;
	
	public ExpressionBool(CodePosition position, IScopeMethod environment, boolean value) {
		super(position, environment);
		
		this.value = value;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().BOOL;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			if (value) {
				output.iConst1();
			} else {
				output.iConst0();
			}
		}
	}

	@Override
	public void compileElse(Label onElse, MethodOutput method) {
		if (!value) {
			method.goTo(onElse);
		}
	}
}
