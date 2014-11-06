/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class ExpressionListSet extends Expression {
	private final Expression list;
	private final Expression index;
	private final Expression value;
	
	public ExpressionListSet(CodePosition position, IScopeMethod environment, Expression list, Expression index, Expression value) {
		super(position, environment);
		
		this.list = list;
		this.index = index;
		this.value = value;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			value.compile(result, output);
			list.compile(result, output);
			index.compile(result, output);
			
			if (value.getType().isLarge()) {
				output.dup2X2();
			} else {
				output.dupX2();
			}
		} else {
			list.compile(result, output);
			index.compile(result, output);
			value.compile(result, output);
		}
		
		output.invokeInterface(List.class, "set", Object.class, int.class, Object.class);
	}

	@Override
	public ZenType getType() {
		return value.getType();
	}
}
