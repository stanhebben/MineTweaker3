/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ExpressionListLength extends Expression {
	private final Expression value;

	public ExpressionListLength(ZenPosition position, IScopeMethod environment, Expression value) {
		super(position, environment);

		this.value = value;
	}

	@Override
	public ZenType getType() {
		return getEnvironment().getTypes().INT;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		value.compile(result, output);

		if (result) {
			output.invokeInterface(List.class, "size", int.class);
		}
	}
}
