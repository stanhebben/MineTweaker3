package org.openzen.zencode.symbolic.statement;

import java.util.List;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.util.CodePosition;

public class StatementBlock<E extends IPartialExpression<E>> extends Statement<E>
{
	private final List<Statement<E>> statements;
	
	public StatementBlock(CodePosition position, IMethodScope<E> environment, List<Statement<E>> statements)
	{
		super(position, environment);
		
		this.statements = statements;
	}
	
	public List<Statement<E>> getStatements()
	{
		return statements;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onBlock(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		for (int i = statements.size() - 1; i >= 0; i--) {
			next = statements.get(i).createFlowBlock(next, builder);
		}
		
		return next;
	}
}
