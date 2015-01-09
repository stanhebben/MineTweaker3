package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.ReturnFlowInstruction;
import org.openzen.zencode.util.CodePosition;

public class StatementReturn<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E expression;

	public StatementReturn(CodePosition position, IMethodScope<E> environment, E expression)
	{
		super(position, environment);

		this.expression = expression;
	}

	public E getExpression()
	{
		return expression;
	}

	@Override
	public boolean isReturn()
	{
		return false;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onReturn(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		FlowBlock<E> result = new FlowBlock<E>();
		return result.prependInstruction(new ReturnFlowInstruction<E>(getPosition(), expression));
	}
}
