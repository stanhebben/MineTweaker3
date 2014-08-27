/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.Map;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;

/**
 *
 * @author Stanneke
 */
public class ExpressionMapSize extends Expression {
	private final Expression map;
	
	public ExpressionMapSize(ZenPosition position, IScopeMethod environment, Expression map) {
		super(position, environment);
		
		this.map = map;
	}

	@Override
	public ZenType getType() {
		return getEnvironment().getTypes().INT;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		map.compile(result, output);
		
		if (result) {
			output.invokeInterface(internal(Map.class), "size", "()I");
		}
	}
}
