/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stan
 */
public class ExpressionStringIndex extends Expression {
	private final Expression source;
	private final Expression index;
	
	public ExpressionStringIndex(ZenPosition position, IScopeMethod environment, Expression source, Expression index) {
		super(position, environment);
		
		this.source = source;
		this.index = index;
	}
	
	@Override
	public ZenType getType() {
		return getEnvironment().getTypes().STRING;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		source.compile(result, output);
		index.compile(result, output);
		
		if (result) {
			output.dup();
			output.iConst1();
			output.iAdd();
			output.invokeVirtual(String.class, "substring", String.class, int.class, int.class);
		}
	}
}
