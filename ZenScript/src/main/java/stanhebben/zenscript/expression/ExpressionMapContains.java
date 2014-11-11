/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;
import static org.openzen.zencode.util.ZenTypeUtil.internal;

/**
 *
 * @author Stanneke
 */
public class ExpressionMapContains extends Expression {
	private final Expression map;
	private final Expression key;
	
	public ExpressionMapContains(CodePosition position, IScopeMethod environment, Expression map, Expression key) {
		super(position, environment);
		
		this.map = map;
		this.key = key;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().ANY;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		map.compile(result, output);
		key.compile(result, output);

		if (result) {
			output.invokeInterface(internal(Map.class), "containsKey", "()Ljava/lang/Object;");
		}
	}
}
