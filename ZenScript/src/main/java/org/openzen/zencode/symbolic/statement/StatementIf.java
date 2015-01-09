package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.symbolic.statement.graph.IfFlowInstruction;
import org.openzen.zencode.symbolic.type.ITypeInstance;
import org.openzen.zencode.util.CodePosition;

public class StatementIf<E extends IPartialExpression<E, T>, T extends ITypeInstance<E, T>> extends Statement<E, T>
{
	private final E condition;
	private final Statement<E, T> onThen;
	private final Statement<E, T> onElse;

	public StatementIf(CodePosition position, IMethodScope<E, T> environment, E condition, Statement<E, T> onThen, Statement<E, T> onElse)
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

	public Statement<E, T> getThen()
	{
		return onThen;
	}

	public Statement<E, T> getElse()
	{
		return onElse;
	}

	@Override
	public <U> U process(IStatementProcessor<E, T, U> processor)
	{
		return processor.onIf(this);
	}

	@Override
	public FlowBlock<E, T> createFlowBlock(FlowBlock<E, T> next, FlowBuilder<E, T> builder)
	{
		IAny conditionConstant = condition.getCompileTimeValue();
		if (conditionConstant != null && conditionConstant.canCastImplicit(boolean.class)) {
			if (conditionConstant.asBool()) {
				return onThen.createFlowBlock(next, builder);
			} else {
				return onElse == null ? next : onElse.createFlowBlock(next, builder);
			}
		}
		
		FlowBlock<E, T> onThenBlockEnd = new FlowBlock<E, T>();
		FlowBlock<E, T> onThenBlockStart = onThen.createFlowBlock(onThenBlockEnd, builder);
		if (onThenBlockEnd.doesFallthrough())
			onThenBlockEnd.addOutgoing(next);
		
		FlowBlock<E, T> onElseBlockStart = next;
		if (onElse != null) {
			FlowBlock<E, T> onElseBlockEnd = new FlowBlock<E, T>();
			onElseBlockStart = onElse.createFlowBlock(onElseBlockEnd, builder);
			
			if (onElseBlockEnd.doesFallthrough())
				onElseBlockEnd.addOutgoing(next);
		}
		
		FlowBlock<E, T> conditionBlock = new FlowBlock<E, T>();
		conditionBlock.prependInstruction(new IfFlowInstruction<E, T>(
				condition,
				onThenBlockStart,
				onElseBlockStart));
		conditionBlock.addOutgoing(onThenBlockStart);
		conditionBlock.addOutgoing(onElseBlockStart);
		
		return conditionBlock;
	}
}
