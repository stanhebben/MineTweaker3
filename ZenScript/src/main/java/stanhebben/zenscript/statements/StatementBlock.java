package stanhebben.zenscript.statements;

import java.util.List;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class StatementBlock extends Statement {
	private final List<Statement> statements;
	
	public StatementBlock(CodePosition position, IScopeMethod environment, List<Statement> statements) {
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
