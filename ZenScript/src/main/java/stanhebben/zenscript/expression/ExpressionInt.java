/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.runtime.IAny;
import zenscript.symbolic.TypeRegistry;
import zenscript.util.ZenPosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionInt extends Expression {
	private final long value;
	private final ZenType type;
	
	public ExpressionInt(ZenPosition position, IScopeMethod environment, long value, ZenType type) {
		super(position, environment);
		
		this.value = value;
		this.type = type;
	}

	@Override
	public Expression cast(ZenPosition position, ZenType type) {
		if (type == this.type) return this;
		
		switch (type.getNumberType()) {
			case IAny.NUM_BYTE:
			case IAny.NUM_SHORT:
			case IAny.NUM_INT:
			case IAny.NUM_LONG:
				return new ExpressionInt(getPosition(), getEnvironment(), value, type);
			case IAny.NUM_FLOAT:
			case IAny.NUM_DOUBLE:
				return new ExpressionFloat(getPosition(), getEnvironment(), value, type);
		}
		
		return super.cast(position, type);
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (!result) return;
		
		TypeRegistry types = getEnvironment().getTypes();
		
		if (type == types.BYTE) {
			output.biPush((byte) value);
		} else if (type == types.SHORT) {
			output.siPush((short) value);
		} else if (type == types.INT) {
			output.constant((int) value);
		} else if (type == types.LONG) {
			output.constant(value);
		} else {
			throw new RuntimeException("Internal compiler error: int constant type is not an int");
		}
	}
}
