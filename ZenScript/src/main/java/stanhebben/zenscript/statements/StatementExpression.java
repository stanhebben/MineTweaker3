package stanhebben.zenscript.statements;

import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class StatementExpression extends Statement {
	private final Expression expression;
	
	public StatementExpression(CodePosition position, IScopeMethod environment, Expression expression) {
		super(position, environment);
		
		this.expression = expression;
	}

	@Override
	public void compile(MethodOutput output) {
		output.position(getPosition());
		expression.compile(false, output);
	}
}
