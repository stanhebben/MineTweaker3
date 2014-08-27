package stanhebben.zenscript.statements;

import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

public class StatementExpression extends Statement {
	private final Expression expression;
	
	public StatementExpression(ZenPosition position, IScopeMethod environment, Expression expression) {
		super(position, environment);
		
		this.expression = expression;
	}

	@Override
	public void compile(MethodOutput output) {
		output.position(getPosition());
		expression.compile(false, output);
	}
}
