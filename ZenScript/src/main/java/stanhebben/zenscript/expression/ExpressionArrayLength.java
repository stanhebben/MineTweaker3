/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionArrayLength extends Expression {
	private final Expression value;
	
	public ExpressionArrayLength(CodePosition position, IScopeMethod environment, Expression value) {
		super(position, environment);
		
		this.value = value;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().INT;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		value.compile(result, output);
		
		if (result) {
			output.arrayLength();
		}
	}
}
