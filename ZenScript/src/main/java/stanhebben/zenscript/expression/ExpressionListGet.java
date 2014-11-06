package stanhebben.zenscript.expression;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeArrayList;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stan
 */
public class ExpressionListGet extends Expression {
	private final Expression list;
	private final Expression index;
	private final ZenTypeArrayList type;
	
	public ExpressionListGet(CodePosition position, IScopeMethod environment, Expression list, Expression index) {
		super(position, environment);
		
		this.list = list;
		this.index = index;
		type = (ZenTypeArrayList) list.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		list.compile(result, output);
		index.compile(result, output);
		
		if (result) {
			output.invokeInterface(List.class, "get", Object.class, int.class);
			output.checkCast(type.getBaseType().toJavaClass());
		}
	}

	@Override
	public ZenType getType() {
		return type.getBaseType();
	}
}
