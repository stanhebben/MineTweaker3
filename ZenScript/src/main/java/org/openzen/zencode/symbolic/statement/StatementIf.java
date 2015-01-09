package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.IfFlowInstruction;
import org.openzen.zencode.util.CodePosition;

public class StatementIf<E extends IPartialExpression<E>> extends Statement<E>
{
	private final E condition;
	private final Statement<E> onThen;
	private final Statement<E> onElse;

	public StatementIf(CodePosition position, IMethodScope<E> environment, E condition, Statement<E> onThen, Statement<E> onElse)
	{
		super(position, environment);

		this.condition = condition;
		this.onThen = onThen;
		this.onElse = onElse;
	}

	public E getCondition()
	{
		return condition;
	}

	public Statement<E> getThen()
	{
		return onThen;
	}

	public Statement<E> getElse()
	{
		return onElse;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onIf(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		IAny conditionConstant = condition.getCompileTimeValue();
		if (conditionConstant != null && conditionConstant.canCastImplicit(boolean.class)) {
			if (conditionConstant.asBool()) {
				return onThen.createFlowBlock(next, builder);
			} else {
				return onElse == null ? next : onElse.createFlowBlock(next, builder);
			}
		}
		
		FlowBlock<E> onThenBlockEnd = new FlowBlock<E>();
		FlowBlock<E> onThenBlockStart = onThen.createFlowBlock(onThenBlockEnd, builder);
		if (onThenBlockEnd.doesFallthrough())
			onThenBlockEnd.addOutgoing(next);
		
		FlowBlock<E> onElseBlockStart = next;
		if (onElse != null) {
			FlowBlock<E> onElseBlockEnd = new FlowBlock<E>();
			onElseBlockStart = onElse.createFlowBlock(onElseBlockEnd, builder);
			
			if (onElseBlockEnd.doesFallthrough())
				onElseBlockEnd.addOutgoing(next);
		}
		
		FlowBlock<E> conditionBlock = new FlowBlock<E>();
		conditionBlock.prependInstruction(new IfFlowInstruction<E>(
				condition,
				onThenBlockStart,
				onElseBlockStart));
		conditionBlock.addOutgoing(onThenBlockStart);
		conditionBlock.addOutgoing(onElseBlockStart);
		
		return conditionBlock;
	}
}
