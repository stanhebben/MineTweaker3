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
public class ExpressionArraySet extends Expression {
	private final Expression array;
	private final Expression index;
	private final Expression value;
	
	public ExpressionArraySet(ZenPosition position, IScopeMethod environment, Expression array, Expression index, Expression value) {
		super(position, environment);
		
		this.array = array;
		this.index = index;
		this.value = value;
	}
	
	@Override
	public ZenType getType() {
		return value.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			value.compile(result, output);
			array.compile(result, output);
			index.compile(result, output);
			
			if (value.getType().isLarge()) {
				output.dup2X2();
			} else {
				output.dupX2();
			}
		} else {
			array.compile(result, output);
			index.compile(result, output);
			value.compile(result, output);
		}
		
		output.arrayStore(value.getType().toASMType());
	}
}
