package stanhebben.zenscript.statements;

import java.util.List;
import stanhebben.zenscript.compiler.IScopeMethod;
import stanhebben.zenscript.util.MethodOutput;
import zenscript.util.ZenPosition;

public class StatementBlock extends Statement {
	private final List<Statement> statements;
	
	public StatementBlock(ZenPosition position, IScopeMethod environment, List<Statement> statements) {
		super(position, environment);
		
		this.statements = statements;
	}

	@Override
	public void compile(MethodOutput output) {
		for (Statement statement : statements) {
			statement.compile(output);
			if (statement.isReturn()) {
				return;
			}
		}
	}
}
