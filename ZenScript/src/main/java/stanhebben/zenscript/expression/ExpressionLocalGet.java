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
public class ExpressionLocalGet extends Expression
{
	private final SymbolLocal variable;

	public ExpressionLocalGet(CodePosition position, IScopeMethod environment, SymbolLocal variable)
	{
		super(position, environment);

		this.variable = variable;
	}

	@Override
	public ZenType getType()
	{
		return variable.getType();
	}

	@Override
	public void compile(boolean result, MethodOutput output)
	{
		int local = output.getLocal(variable);
		output.load(variable.getType().toASMType(), local);
	}
}
