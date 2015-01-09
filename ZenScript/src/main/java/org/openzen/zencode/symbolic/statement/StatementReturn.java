package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.ReturnFlowInstruction;
import org.openzen.zencode.symbolic.type.IZenType;
import org.openzen.zencode.util.CodePosition;

public class StatementReturn<E extends IPartialExpression<E, T>, T extends IZenType<E, T>> extends Statement<E, T>
{
	private final E expression;

	public StatementReturn(CodePosition position, IMethodScope<E, T> environment, E expression)
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
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onReturn(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		FlowBlock<E, T> result = new FlowBlock<E, T>();
		return result.prependInstruction(new ReturnFlowInstruction<E, T>(getPosition(), expression));
	}
}
