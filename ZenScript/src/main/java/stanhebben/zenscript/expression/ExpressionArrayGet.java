/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArray;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpressionArrayGet extends Expression {
	private final Expression array;
	private final Expression index;
	private final ZenType baseType;
	
	public ExpressionArrayGet(CodePosition position, IScopeMethod environment, Expression array, Expression index) {
		super(position, environment);
		
		this.array = array;
		this.index = index;
		this.baseType = ((ZenTypeArray) array.getType()).getBaseType();
	}

	@Override
	public ZenType getType() {
		return baseType;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		array.compile(result, output);
		index.compile(result, output);
		
		if (result) {
			output.arrayLoad(baseType.toASMType());
		}
	}
}
