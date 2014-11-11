/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionStringConcat extends Expression {
	private final List<Expression> values;
	
	public ExpressionStringConcat(CodePosition position, IScopeMethod environment, List<Expression> values) {
		super(position, environment);
		
		this.values = values;
	}
	
	public void add(Expression value) {
		values.add(value);
	}

	@Override
	public ZenType getType() {
		return getScope().getTypes().STRING;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (result) {
			// Step 1: construct StringBuilder
			output.newObject(StringBuilder.class);
			output.dup();
			output.construct(StringBuilder.class);
			
			// Step 2: concatenate Strings
			for (Expression value : values) {
				value.compile(true, output);
				output.invoke(StringBuilder.class, "append", StringBuilder.class, String.class);
			}
			
			// Step 3: return String
			output.invoke(StringBuilder.class, "toString", String.class);
		} else {
			for (Expression value : values) {
				value.compile(false, output);
			}
		}
	}
}
