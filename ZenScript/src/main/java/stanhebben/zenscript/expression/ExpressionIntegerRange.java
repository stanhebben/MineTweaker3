/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.runtime.Range;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionIntegerRange extends Expression {
	private final Expression from;
	private final Expression to;

	public ExpressionIntegerRange(CodePosition position, IScopeMethod environment, Expression from, Expression to) {
		super(position, environment);
		
		this.from = from;
		this.to = to;
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().RANGE;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			output.newObject(Range.class);
		}
		
		from.compile(result, output);
		to.compile(result, output);
		
		if (result) {
			output.construct(Range.class, int.class, int.class);
		}
	}
}
