/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openzen.zencode.symbolic.statement;

import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.statement.graph.FlowBlock;
import org.openzen.zencode.symbolic.statement.graph.FlowBuilder;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 * @param <E>
 */
public class StatementBreak<E extends IPartialExpression<E>> extends Statement<E>
{
	private final Statement<E> target;

	public StatementBreak(CodePosition position, IMethodScope<E> scope, Statement<E> target)
	{
		super(position, scope);

		this.target = target;
	}
	
	public Statement<E> getTarget()
	{
		return target;
	}

	@Override
	public <U> U process(IStatementProcessor<E, U> processor)
	{
		return processor.onBreak(this);
	}

	@Override
	public FlowBlock<E> createFlowBlock(FlowBlock<E> next, FlowBuilder<E> builder)
	{
		return builder.getBreakLabel(target);
	}
}
