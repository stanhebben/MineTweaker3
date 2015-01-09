package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

public class StatementBlock<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final List<Statement<E, T>> statements;
	
	public StatementBlock(CodePosition position, IMethodScope<E, T> environment, List<Statement<E, T>> statements)
	{
		super(position, environment);
		
		this.statements = statements;
	}
	
	public List<Statement<E, T>> getStatements()
	{
		return statements;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onBlock(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		for (int i = statements.size() - 1; i >= 0; i--) {
			next = statements.get(i).createFlowBlock(next, builder);
		}
		
		return next;
	}
}
