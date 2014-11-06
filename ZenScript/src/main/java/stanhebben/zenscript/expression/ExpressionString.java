/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionString extends Expression {
	private final String value;
	
	public ExpressionString(CodePosition position, IScopeMethod environment, String value) {
		super(position, environment);
		
		this.value = value;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().STRING;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			output.constant(value);
		}
	}
}
