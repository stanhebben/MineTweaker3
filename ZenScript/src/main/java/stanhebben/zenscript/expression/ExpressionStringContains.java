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
 * @author Stan
 */
public class ExpressionStringContains extends Expression {
	private final Expression haystack;
	private final Expression needle;
	
	public ExpressionStringContains(CodePosition position, IScopeMethod environment, Expression haystack, Expression needle) {
		super(position, environment);
		
		this.haystack = haystack;
		this.needle = needle;
	}
	
	@Override
	public ZenType getType() {
		return getScope().getTypes().BOOL;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		haystack.compile(result, output);
		needle.compile(result, output);
		
		if (result) {
			output.invokeVirtual(String.class, "contains", CharSequence.class);
		}
	}
}
