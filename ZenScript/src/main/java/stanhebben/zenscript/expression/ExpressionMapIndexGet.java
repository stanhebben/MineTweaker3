/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.Map;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeAssociative;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;
import static stanhebben.zenscript.util.ZenTypeUtil.internal;

/**
 *
 * @author Stanneke
 */
public class ExpressionMapIndexGet extends Expression {
	private final Expression map;
	private final Expression index;
	
	private final ZenType type;
	
	public ExpressionMapIndexGet(CodePosition position, IScopeMethod environment, Expression map, Expression index) {
		super(position, environment);
		
		this.map = map;
		this.index = index;
		
		type = ((ZenTypeAssociative) map.getType()).getValueType();
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			map.compile(result, output);
			index.compile(result, output);
			
			output.invokeInterface(
					internal(Map.class),
					"get",
					"(Ljava/lang/Object;)Ljava/lang/Object;");
			output.checkCast(type.toASMType().getInternalName());
		}
	}
}
