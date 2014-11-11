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
public class ExpressionFloat extends Expression {
	private final double value;
	private final ZenType type;
	
	public ExpressionFloat(CodePosition position, IScopeMethod environment, double value, ZenType type) {
		super(position, environment);
		
		this.value = value;
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (!result) return;
		
		if (type == getScope().getTypes().FLOAT) {
			output.constant((float) value);
		} else if (type == getScope().getTypes().DOUBLE) {
			output.constant(value);
		} else {
			throw new RuntimeException("Internal compiler error: source type is not a floating point type");
		}
	}
}
