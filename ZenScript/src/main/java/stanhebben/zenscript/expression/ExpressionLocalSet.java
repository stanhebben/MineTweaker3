/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.symbols.SymbolLocal;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stanneke
 */
public class ExpressionLocalSet extends Expression {
	private final SymbolLocal variable;
	private final Expression value;
	
	public ExpressionLocalSet(CodePosition position, IScopeMethod environment, SymbolLocal variable, Expression value) {
		super(position, environment);
		
		this.variable = variable;
		this.value = value;
	}
	
	@Override
	public ZenType getType() {
		return variable.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		int local = output.getLocal(variable);
		
		value.compile(true, output);
		if (result) {
			output.dup();
		}
		output.store(variable.getType().toASMType(), local);
	}
}
