/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeVoid;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;

/**
 *
 * @author Stanneke
 */
public class ExpressionMapIndexSet extends Expression {
	private final Expression map;
	private final Expression index;
	private final Expression value;
	
	public ExpressionMapIndexSet(CodePosition position, IScopeMethod environment, Expression map, Expression index, Expression value) {
		super(position, environment);
		
		this.map = map;
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
			map.compile(result, output);
			index.compile(result, output);
			
			if (value.getType().isLarge()) {
				output.dup2X2();
			} else {
				output.dupX2();
			}
			
			output.invokeInterface(
					internal(Map.class),
					"put",
					"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
		} else {
			map.compile(result, output);
			index.compile(result, output);
			value.compile(result, output);
			
			output.invokeInterface(
					internal(Map.class),
					"put",
					"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
		}
	}
}
