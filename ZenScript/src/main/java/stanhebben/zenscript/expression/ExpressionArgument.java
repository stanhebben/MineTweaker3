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
public class ExpressionArgument extends Expression {
	private final int id;
	private final ZenType type;
	
	public ExpressionArgument(CodePosition position, IScopeMethod environment, int id, ZenType type) {
		super(position, environment);
		
		this.id = id;
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		output.load(type.toASMType(), id);
	}
}
